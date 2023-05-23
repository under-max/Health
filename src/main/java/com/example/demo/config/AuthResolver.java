package com.example.demo.config;


import com.example.demo.entity.AuthUser;
import com.example.demo.exception.Unauthorized;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;
import java.util.Base64;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthResolver implements HandlerMethodArgumentResolver {

    @Value("${jtw.key}")
    public String key;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthUser.class);
    }

    @Override
    public AuthUser resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("AuthResolver 실행");
        String accessJws = webRequest.getHeader("Authorization");
        String refreshJws = webRequest.getHeader("RefreshToken");
        String jws = "";
        //refreshToken 검증 및 처리"Bearer"+
        if(refreshJws != null && !refreshJws.equals("")){
           jws = refreshJws;
           try{
               Jws<Claims> claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(key)))
//                        .setAllowedClockSkewSeconds(60) // 1분까지는 시간차이를 허용
                        .build()
                        .parseClaimsJws(jws);
                String userId = claims.getBody().getSubject();
                return AuthUser.builder()
                        .userId(Long.valueOf(userId))
                        .build();
           } catch (ExpiredJwtException e){
               throw new Unauthorized("로그인 인증이 만료되었습니다.");
           }
        } else if (accessJws == null || accessJws.equals("")) {
            throw new Unauthorized("로그인이 필요합니다.");
        } else {
            jws = accessJws;
        }

        // 일반 accessToken 검증 및 처리
        try {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(key)))
//                .setAllowedClockSkewSeconds(60) // 1분까지는 시간차이를 허용
                .build()
                .parseClaimsJws(jws);
        String userId = claims.getBody().getSubject();
        return AuthUser.builder()
                .userId(Long.valueOf(userId))
                .build();

        } catch (ExpiredJwtException e) {
            throw new Unauthorized("로그인 인증이 만료되었습니다.");
        } catch (JwtException e) {
            throw new Unauthorized("로그인이 필요합니다.");
        }
    }
}

