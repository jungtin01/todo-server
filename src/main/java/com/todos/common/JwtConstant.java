package com.todos.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConstant {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    
    private static final long numOfDays = 3;
    public static final long EXPIRATION_TIME = 1000*60*60*24 * numOfDays;
    
    @Value("${jwt.token.secret}")
    private String TOKEN_SECRET;
    
    public String getTokenSecret() {
        return TOKEN_SECRET;
    }
}
