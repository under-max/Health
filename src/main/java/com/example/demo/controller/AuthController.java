package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.request.Login;
import com.example.demo.response.AuthResponse;
import com.example.demo.response.AuthUserResponse;
import com.example.demo.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${jtw.key}")
    public String key;

    @PostMapping("/auth/login")
    public AuthResponse login(@RequestBody Login login) {
        AuthUser loginUser = authService.signIn(login);

        String jws = Jwts.builder()
                .setSubject(String.valueOf(loginUser.getUserId()))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(key)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ( 30 * 60 * 1000L))) // 30분
                .compact();

        String refreshJws = Jwts.builder()
                .setSubject(String.valueOf(loginUser.getUserId()))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(key)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (30L * 24L * 60L * 60L * 1000L))) // 30일
                .compact();

        return AuthResponse.builder()
                .accessToken(jws)
                .refreshToken(refreshJws)
                .build();
    }

    @PostMapping("/auth/login/refresh")
    public AuthResponse refresh(AuthUser authUser) {

        String jws = Jwts.builder()
                .setSubject(String.valueOf(authUser.getUserId()))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(key)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (30 * 60 * 1000L))) // 30분
                .compact();

        String refreshJws = Jwts.builder()
                .setSubject(String.valueOf(authUser.getUserId()))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(key)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (30L * 24L * 60L * 60L * 1000L))) // 30일
                .compact();

        return AuthResponse.builder()
                .accessToken(jws)
                .refreshToken(refreshJws)
                .build();
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 클라이언트의 세션을 무효화하여 로그아웃 처리
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 쿠키를 삭제하여 클라이언트의 세션 쿠키도 제거
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    @PostMapping("/auth/user/{service}")
    public AuthUserResponse authUser(@RequestBody AuthUserResponse authUserResponse, @PathVariable String service, AuthUser authUser) {
        log.info(">>>auth/user/ {}", service);
        AuthUserResponse authedUserResponse = authService.authUser(AuthUserResponse.builder()
                .email(authUserResponse.getEmail())
                .postId(authUserResponse.getPostId())
                .userId(authUserResponse.getUserId())
                .password(authUserResponse.getPassword())
                .service(service)
                .nickName(authUserResponse.getNickName())
                .authedUserId(authUser.getUserId())
                .build());

        return authedUserResponse;
    }

    @PostMapping("/auth/signup/{service}")
    public AuthUserResponse authUser(@RequestBody AuthUserResponse authUser, @PathVariable String service) {
        log.info("auth/signup/ {}", service);
        AuthUserResponse authedUser = authService.authUser(AuthUserResponse.builder()
                .email(authUser.getEmail())
                .nickName(authUser.getNickName())
                .service(service)
                .build());
        return authedUser;
    }
}