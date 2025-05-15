package com.example.demo.configs;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "hovantrinh2"; // Thay đổi mật khẩu theo ý bạn
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println(encodedPassword); // In ra mật khẩu đã mã hóa
    }
}
