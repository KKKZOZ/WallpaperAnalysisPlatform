package org.jff.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jff.global.ResponseVO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Login failed");
        if (!response.isCommitted()) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(403);
            ResponseVO res  = new ResponseVO(403, (String) request.getAttribute("info"));
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            String content = JSON.toJSONString(res);
            response.getWriter().write(content);

        }
    }
}
