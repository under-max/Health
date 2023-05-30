package com.example.demo.service;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.User;
import com.example.demo.exception.*;
import com.example.demo.mapper.UserMapper;
import com.example.demo.request.Login;
import com.example.demo.response.AuthUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthUser signIn(Login request) {

        User user = userMapper.findByEmail(request.getEmail()).orElseThrow(InvalidSigninInformation::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidSigninInformation();
        }
        return AuthUser.builder().userId(user.getId()).build();
    }
    public AuthUserResponse authUser(AuthUserResponse authUserResponse) {
        switch (authUserResponse.getService()) {
            case "email":
                if (authUserResponse.getEmail().equals("")) {
                    throw new Unauthorized("이메일을 입력하세요.");
                }
                if (userMapper.findByEmail(authUserResponse.getEmail()).isEmpty()) {
                    return AuthUserResponse.builder()
                            .authResult(true)
                            .build();
                } else {
                    return AuthUserResponse.builder()
                            .authResult(false)
                            .build();
                }
            case "nickName":
                if (authUserResponse.getNickName().equals("")) {
                    throw new Unauthorized("닉네임을 입력하세요.");
                }

                if (!userMapper.findByNickName(authUserResponse.getNickName()).isEmpty()
                        && !userMapper.findByNickName(authUserResponse.getNickName()).orElseThrow().getEmail().equals(authUserResponse.getEmail())) {
                    throw new DuplicateEmail("중복된 닉네임입니다.");
                }
                return AuthUserResponse.builder()
                        .nickName(authUserResponse.getNickName())
                        .authResult(true)
                        .build();

            case "password":
                User user1 = userMapper.findById(authUserResponse.getAuthedUserId()).orElseThrow(UserNotFound::new);
                if (passwordEncoder.matches(authUserResponse.getPassword(), user1.getPassword())) {
                    return AuthUserResponse.builder()
                            .authResult(true)
                            .build();
                } else {
                    return AuthUserResponse.builder()
                            .authResult(false)
                            .build();
                }
            case "login":
                return AuthUserResponse.builder()
                        .authResult(true)
                        .build();
            case "auth":
                User authedUser = userMapper.findById(authUserResponse.getAuthedUserId()).orElseThrow();
                log.info("{}",authedUser.getBirthDate());
                if(authedUser.getBirthDate() != null){
                    return AuthUserResponse.builder()
                            .userId(authedUser.getId())
                            .email(authedUser.getEmail())
                            .nickName(authedUser.getNickName())
                            .name(authedUser.getName())
                            .address(authedUser.getAddress())
                            .birthDate(authedUser.getBirthDate().toString())
                            .authResult(true)
                            .build();
                } else {
                return AuthUserResponse.builder()
                        .userId(authedUser.getId())
                        .email(authedUser.getEmail())
                        .nickName(authedUser.getNickName())
                        .name(authedUser.getName())
                        .address(authedUser.getAddress())
                        .authResult(true)
                        .build();
                }
            default:
                throw new InvalidRequest();
        }
    }
}
