package org.jff.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.NotificationEvent;
import org.jff.dto.UserDTO;
import org.jff.global.APIException;
import org.jff.global.ResponseVO;
import org.jff.global.ResultCode;
import org.jff.test.OrderMessageProducer;
import org.jff.utils.JwtUtil;
import org.jff.utils.RedisCache;
import org.jff.utils.SecurityUtil;
import org.jff.vo.UserVO;
import org.springframework.cloud.sleuth.ScopedSpan;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final Tracer tracer;

    private final AuthenticationManager authenticationManager;

    private final OrderMessageProducer orderMessageProducer;
    private final RedisCache redisCache;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseVO login(UserDTO userDTO) {

        ScopedSpan loginSpan = tracer.startScopedSpan("login");
        loginSpan.event("Check if user valid");
        Optional<User> optionalUser = userMapper.findByUsername(userDTO.getUsername());
        if(optionalUser.isEmpty()){
            throw new APIException("用户不存在");
        }
        User user = optionalUser.get();
        if(!user.isEnabled()){
            return new ResponseVO(ResultCode.USER_NOT_ACTIVATED);
        }
        String userId = String.valueOf(user.getUserId());
        log.info("userId: {}", userId);
        redisCache.deleteObject("login:"+userId);

        loginSpan.event("Authentication");
        // 创建一个新的token给SpringSecurity进行认证
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());

        Authentication authenticate = authenticationManager.authenticate(token);
        // 认证未通过
        if (!authenticate.isAuthenticated()) {
            return new ResponseVO<>(ResultCode.LOGIN_FAILED);
        }

        loginSpan.event("Create JWT");
        // 认证通过后，根据userId生成一个jwt
        User loginUser = (User) authenticate.getPrincipal();
        userId = String.valueOf(loginUser.getUserId());

        log.info("userId: {}", userId);

        String jwt = JwtUtil.createJWT(JSONObject.toJSONString(loginUser));

        redisCache.setCacheObject("login:"+userId,jwt);
        loginSpan.end();

        return new ResponseVO(ResultCode.SUCCESS, jwt);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if(user == null) {
            throw new APIException("用户名不存在");
        }
        return user;
    }

    public ResponseVO logout() {
        // 获取SecurityContextHolder中的用户id
        // TODO: 封装为一个单独的类
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();

        User loginUser = (User)authentication.getPrincipal();
        String userId = String.valueOf(loginUser.getUserId());

        // 从redis中删除用户信息
        redisCache.deleteObject("login:"+userId);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO register(User user){
        // 考虑以下几种特殊情况
        // 1. 用户名重复
        String username = user.getUsername();
        if(userMapper.findByUsername(username).isPresent()){
            return new ResponseVO(ResultCode.USERNAME_ALREADY_EXIST);
        }


        // 先把密码进行加密
        String originPassword = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // 先设置用户为不可用的状态，等待邮箱验证激活
        user.setEnabled(false);
        userMapper.insert(user);

        User newUser = userMapper
                .selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        Long userId = newUser.getUserId();
        log.info("userId: {}", userId);
        // 直接生成一个六位的随机验证码
        String code = generateActivationCode();
        log.info("code: {}", code);
        // 存入redis中，设置过期时间为10分钟
        redisCache.setCacheObject("activation:" + code, String.valueOf(userId), 10 * 60, TimeUnit.SECONDS);

        NotificationEvent event = new NotificationEvent();
        event.setRecipientId(userId);
        event.setContent(code);
        event.setType(NotificationEvent.ACTIVATION);
        event.setCategory(0);
        orderMessageProducer.sendEvent(event);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO updateUserInfo(User user) {
        userMapper.updateById(user);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public List<UserVO> getUserInfoList(List<Long> userIdList) {
        List<UserVO> list = new ArrayList<>();
        log.info("userIdList: {}", userIdList);
        for(Long userId : userIdList) {
            User user = userMapper.selectById(userId);
            UserVO userVO = new UserVO();
            userVO.setUserId(user.getUserId());
            userVO.setUsername(user.getUsername());
            userVO.setEmail(user.getEmail());
            userVO.setAvatarUrl(user.getAvatarUrl());
            list.add(userVO);
        }
        log.info("list: {}", list);
        return list;
    }

    public ResponseVO send(String msg) {
        orderMessageProducer.sendEvent(msg);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO activateUser(String code) {
        String redisKey = "activation:" + code;
        String value = redisCache.getCacheObject(redisKey);
        if (value == null) {
            return new ResponseVO(ResultCode.ACTIVATION_CODE_EXPIRED);
        }
        Long userId = Long.valueOf(value);
        User user = userMapper.selectById(userId);
        user.setEnabled(true);
        userMapper.updateById(user);
        return new ResponseVO(ResultCode.SUCCESS);
    }
    public String generateActivationCode() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public UserVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getUserId());
        userVO.setUsername(user.getUsername());
        userVO.setEmail(user.getEmail());
        userVO.setAvatarUrl(user.getAvatarUrl());
        return userVO;
    }
}
