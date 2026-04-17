package com.clt.utils;

import com.clt.config.JwtConfig;
import com.clt.entity.User;
import com.clt.enums.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Autowired
    private JwtConfig jwtConfig;

    // 生成 Token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                .claim("avatar", user.getAvatar())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
    }

    // 解析 Token 获取用户信息
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    // 验证 Token 是否有效
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 使 Token 失效
    public void invalidateToken(String token) {
        parseToken(token).setExpiration(new Date(0));
    }

    // 验证用户身份
    public boolean isUser(String token) {
        return parseToken(token).get("role", Integer.class) == UserType.USER.code;
    }
}
