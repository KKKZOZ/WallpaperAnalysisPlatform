package org.jff.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jff.global.ResponseVO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("权限错误");
        if (!response.isCommitted()) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(403);
            ResponseVO res  = new ResponseVO(403, "你没有该权限");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            String content = JSON.toJSONString(res);
            response.getWriter().write(content);

        }
    }
}
