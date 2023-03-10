package org.jff.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
        // 用户密码
        String password = "kkkzoz";
        // 创建密码加密的对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 密码加密
        String newPassword = passwordEncoder.encode(password);
        System.out.println("加密后的密码为：" + newPassword);

        // 校验这两个密码是否是同一个密码
        // matches方法第一个参数是原密码，第二个参数是加密后的密码
        boolean matches = passwordEncoder.matches(password, newPassword);
        System.out.println("两个密码一致:" + matches);
    }
}
