package org.jff.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Component
public class MyLogGateWayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Time: {}  New request: {}, Method:{} Source IP :{}",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                exchange.getRequest().getPath().value(),
                exchange.getRequest().getMethod().name(),
                exchange.getRequest().getRemoteAddress().toString());
        return chain.filter(exchange);
    }

    /**
     * 加载过滤器顺序，数字越小优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 2;
    }
}