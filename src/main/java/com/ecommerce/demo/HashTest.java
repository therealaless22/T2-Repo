package com.ecommerce.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashAdmin = encoder.encode("UPN2100");
        System.out.println("NUEVO HASH ADMIN: " + hashAdmin);
    }
}