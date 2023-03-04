package org.jff.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.dto.UserDTO;
import org.jff.global.APIException;
import org.jff.global.ResponseVO;
import org.jff.global.ResultCode;
import org.jff.test.OrderMessageProducer;
import org.jff.utils.JwtUtil;
import org.jff.utils.RedisCache;
import org.jff.utils.SecurityUtil;
import org.jff.vo.UserVO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final SecurityUtil securityUtil;

    private final OrderMessageProducer orderMessageProducer;
    private final RedisCache redisCache;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseVO login(UserDTO userDTO) {
        Optional<User> optionalUser = userMapper.findByUsername(userDTO.getUsername());
        if(!optionalUser.isPresent()){
            throw new APIException("用户不存在");
        }
        String userId = String.valueOf(optionalUser.get().getUserId());
        log.info("userId: {}", userId);
        redisCache.deleteObject("login:"+userId);

        // 创建一个新的token给SpringSecurity进行认证
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());

        Authentication authenticate = authenticationManager.authenticate(token);
        // 认证未通过
        if (!authenticate.isAuthenticated()) {
            return new ResponseVO<>(ResultCode.LOGIN_FAILED);
        }

        // 认证通过后，根据userId生成一个jwt
        User loginUser = (User) authenticate.getPrincipal();
        userId = String.valueOf(loginUser.getUserId());

        log.info("userId: {}", userId);

        String jwt = JwtUtil.createJWT(JSONObject.toJSONString(loginUser));

        redisCache.setCacheObject("login:"+userId,jwt);

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
        // 先把密码进行加密
        String originPassword = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        UserDTO userDTO = UserDTO.builder()
                .username(user.getUsername())
                .password(originPassword)
                .build();
        return this.login(userDTO);
    }

    public ResponseVO updateUserInfo(User user) {
        userMapper.updateById(user);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public List<UserVO> getUserInfoList(List<Long> userIdList) {
        List<User> userList = userMapper.selectBatchIds(userIdList);
        List<UserVO> list = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            userVO.setUserId(user.getUserId());
            userVO.setUsername(user.getUsername());
            userVO.setAvatarUrl(user.getAvatarUrl());
            return userVO;
        }).toList();
        return list;
    }

    public ResponseVO send(String msg) {
        orderMessageProducer.sendMsg(msg);
        return new ResponseVO(ResultCode.SUCCESS);
    }
}
