package org.jff.filter;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.User;
import org.jff.global.APIException;
import org.jff.utils.JwtUtil;
import org.jff.utils.RedisCache;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationTokenFilter implements GlobalFilter, Ordered {

    private final RedisCache redisCache;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // APIFOX 放行
        if(!exchange.getRequest().getHeaders().containsKey("token")){
            String path = exchange.getRequest().getPath().value();
            if(path.startsWith("/api/v1/file/storage")
                    ||path.startsWith("/api/v1/user/login")
                    ||path.startsWith("/api/v1/user/activate")
                    ||path.startsWith("/api/v1/user/register")
                    ||path.startsWith("/api/v1/statistics")){
                return chain.filter(exchange);
            }
            else {
                log.info("Header:Token is Not Present");
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }
        String token = exchange.getRequest().getHeaders().get("token").get(0);
        String path = exchange.getRequest().getPath().value();
//        log.info("path: {}",path);
//        log.info("isEmpty: {}",token.isEmpty());
//        log.info("token: {}",token);
        // 前端放行
        if(token.isEmpty() &&
                (path.startsWith("/api/v1/user/login")
                        ||path.startsWith("/api/v1/user/activate")
                        ||path.startsWith("/api/v1/user/register")
                        ||path.startsWith("/api/v1/statistics"))){
            return chain.filter(exchange);
        }
        User user = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            user = JSONObject.parseObject(claims.getSubject(), User.class);
        } catch (Exception e) {
            log.info("token 非法");
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            //TODO: 返回同一结构体
            return exchange.getResponse().setComplete();
        }

        String redisKey = "login:"+user.getUserId();
//        log.info("redisKey: {}",redisKey);
        String tokenInRedis = (String) redisCache.getCacheObject(redisKey);
//        log.info("tokenInRedis: {}",tokenInRedis);
        if(Objects.isNull(user) || !token.equals(tokenInRedis)){
            log.info("Token 已过期");
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            //TODO: 返回同一结构体
            return exchange.getResponse().setComplete();
        }

        ServerHttpRequest request = exchange.getRequest()
                .mutate().header("userId",String.valueOf(user.getUserId()))
                .build();
        ServerWebExchange exchange1 = exchange.mutate().request(request).build();

        return chain.filter(exchange1);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
