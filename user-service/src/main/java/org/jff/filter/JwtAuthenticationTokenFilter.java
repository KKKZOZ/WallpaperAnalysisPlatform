package org.jff.filter;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.global.APIException;
import org.jff.utils.JwtUtil;
import org.jff.user.User;
import org.jff.utils.RedisCache;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


/**
 * 1. 定义Jwt认证过滤器
 *    1. 获取token
 *    2. 解析token
 *    3. 获取其中的userid
 *    4. 从redis中获取用户信息
 *    5. 存入SecurityContextHolder中
 * */
@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    private final RedisCache redisCache;



    @Override
    //过滤器中doFilter方法前面的逻辑是请求进来时执行的内容，doFilter后面的逻辑是响应时执行的内容，直接return了，响应时就不会执行后面的内容了
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws APIException,ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //如果没有token，直接放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        User loginUser = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            loginUser = JSONObject.parseObject(claims.getSubject(), User.class);
        } catch (Exception e) {
            log.info("Token解析失败 {}",token);
            // TODO: 返回对应的错误提示信息
            request.setAttribute("info","token非法");
            throw new APIException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:" + loginUser.getUserId();
        log.info("redisKey: {}",redisKey);
        String tokenInRedis = (String) redisCache.getCacheObject(redisKey);
        if(Objects.isNull(loginUser) || !token.equals(tokenInRedis)){
            request.setAttribute("info","Token已过期");
            throw new APIException("Token已过期");
        }
        //LoginUser implements UserDetails

        //存入SecurityContextHolder
        //TODO:获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        // principal 相当于用户名
        // credentials 相当于密码

        //直接设置已经认证
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
