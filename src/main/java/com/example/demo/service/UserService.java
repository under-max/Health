package com.example.demo.service;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateEmail;
import com.example.demo.exception.UserNotFound;
import com.example.demo.mapper.UserMapper;
import com.example.demo.request.UserDelete;
import com.example.demo.request.UserCreate;
import com.example.demo.request.UserEdit;
import com.example.demo.response.AuthUserResponse;
import com.example.demo.response.UserDetailResponse;
import com.example.demo.response.UserListResponse;
import com.example.demo.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor // final로 생성된 field의 생성자 자동 autowired
@Transactional(rollbackFor = Exception.class)
public class UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public User add(UserCreate userCreate) {
        if (!userMapper.findByEmail(userCreate.getEmail()).isEmpty()) {
            throw new DuplicateEmail();
        }
        if (!userMapper.findByNickName(userCreate.getNickName()).isEmpty()) {
            throw new DuplicateEmail("중복된 닉네임입니다.");
        }
        User user = User.builder()
                .email(userCreate.getEmail())
                .password(passwordEncoder.encode(userCreate.getPassword()))
                .nickName(userCreate.getNickName())
                .name(userCreate.getName())
                .address(userCreate.getAddress())
                .birthDate(userCreate.getBirthDate())
                .build();
        userMapper.save(user);
        return userMapper.findById(user.getId()).orElseThrow(UserNotFound::new);
    }

    public User addSocial(UserCreate userCreate) {
        if (!userMapper.findByNickName(userCreate.getNickName()).isEmpty()
                && !userMapper.findByNickName(userCreate.getNickName()).orElseThrow().getEmail().equals(userCreate.getEmail())) {
            throw new DuplicateEmail("중복된 닉네임입니다.");
        }
        User user = User.builder()
                .id(userCreate.getId())
                .password(passwordEncoder.encode(userCreate.getPassword()))
                .nickName(userCreate.getNickName())
                .name(userCreate.getName())
                .address(userCreate.getAddress())
                .birthDate(userCreate.getBirthDate())
                .build();
        log.info("{}", user);

        userMapper.saveSocial(user);
        return userMapper.findById(user.getId()).orElseThrow(UserNotFound::new);
    }


    public List<User> getUsers(int page) {
        return userMapper.findAll(page);
    }

    public UserResponse get(Long id) {
        User user = userMapper.findById(id).orElseThrow(UserNotFound::new);
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .dateTime(user.getDateTime())
                .build();
    }

    public void edit(UserEdit userEdit) {
        if (!passwordEncoder.matches(userEdit.getPassword(), userMapper.findById(userEdit.getUserId()).orElseThrow().getPassword())) {
            throw new DuplicateEmail("기존 비밀번호를 확인하세요");
        }
        if (!userMapper.findByNickName(userEdit.getNickName()).isEmpty()
                && !userMapper.findByNickName(userEdit.getNickName()).orElseThrow().getId().equals(userEdit.getUserId())) {
            throw new DuplicateEmail("중복된 닉네임입니다.");
        }

        if (userEdit.getNewPassword() != null && !userEdit.getNewPassword().equals("")) {
            userMapper.update(UserEdit.builder()
                    .userId(userEdit.getUserId())
                    .password(passwordEncoder.encode(userEdit.getNewPassword()))
                    .nickName(userEdit.getNickName())
                    .name(userEdit.getName())
                    .address(userEdit.getAddress())
                    .birthDate(userEdit.getBirthDate())
                    .build());
        } else {
            userMapper.update(UserEdit.builder()
                    .userId(userEdit.getUserId())
                    .password(passwordEncoder.encode(userEdit.getPassword()))
                    .nickName(userEdit.getNickName())
                    .name(userEdit.getName())
                    .address(userEdit.getAddress())
                    .birthDate(userEdit.getBirthDate())
                    .build());
        }
    }

    public void delete(UserDelete userDelete) {
        User user = userMapper.findById(userDelete.getAuthedUserId()).orElseThrow();
        userMapper.delete(user.getId());
    }

    public List<UserListResponse> listAllTest() {
        return userMapper.findAllTest();
    }

    public UserDetailResponse getUserTest(Long id) {
        return userMapper.findByIdTest(id);
    }

    public AuthUserResponse kakaoEmailCheck(KakaoLoginService.KakaoAccount kakaoAccount) {
        if (userMapper.findByEmail(kakaoAccount.getEmail()).isEmpty()) {
            //없으면 가입 시키고 true 반환
            userMapper.save(User.builder()
                    .email(kakaoAccount.getEmail())
                    .build());
            User user = userMapper.findByEmail(kakaoAccount.getEmail()).orElseThrow();
            return AuthUserResponse.builder().userId(user.getId()).build();
        }
        //있으면 true 반환
        User user = userMapper.findByEmail(kakaoAccount.getEmail()).orElseThrow();
        return AuthUserResponse.builder().userId(user.getId()).build();
    }
}

