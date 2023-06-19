package com.cl.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/30
 * @description:
 * @version:1.0
 */

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwd = encoder.encode("1234");
        System.out.println(pwd);
    }
}
