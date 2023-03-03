package org.jff.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SecurityUtil {

    public Long getUserId() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUser.getUserId();
    }

}
