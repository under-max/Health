package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Membership;
import com.example.demo.entity.User;
import com.example.demo.request.UserDelete;
import com.example.demo.request.UserCreate;
import com.example.demo.request.UserEdit;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserDetailResponse;
import com.example.demo.response.UserListResponse;
import com.example.demo.response.UserResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    public User addUser(@RequestBody @Valid UserCreate userCreate) {
        log.info("controller>>>{}", userCreate);
        return userService.add(userCreate);
    }

    @PostMapping("/user/social")
    public User addSocailUser(@RequestBody @Valid UserCreate userCreate, AuthUser authUser) {
        userCreate.setId(authUser.getUserId());
        log.info("social User Create>>>{}", userCreate);
        return userService.addSocial(userCreate);
    }

    @GetMapping("/user")
    public List<User> getList(@RequestParam(defaultValue = "1") int page) {
        return userService.getUsers(page);
    }

    @GetMapping("/user/{id}")
    public UserResponse get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PatchMapping("/user")
    public void edit(@RequestBody @Valid UserEdit userEdit, AuthUser authUser) {
        userService.edit(UserEdit.builder()
                .userId(authUser.getUserId())
                .email(userEdit.getEmail())
                .password(userEdit.getPassword())
                .newPassword(userEdit.getNewPassword())
                .nickName(userEdit.getNickName())
                .name(userEdit.getName())
                .address(userEdit.getAddress())
                .birthDate(userEdit.getBirthDate())
                .build());
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable Long id, AuthUser authUser) {
        userService.delete(UserDelete.builder()
                .authedUserId(authUser.getUserId())
                .build());
    }


    // 로그인된 유저 정보 조회
    @GetMapping("/authUser")
    public List<UserListResponse> getUser(AuthUser authUser) {
        return userService.getAuthedUser(authUser.getUserId());
    }


    @GetMapping("/user/get/{id}")
    public UserDetailResponse getUserDetail(@PathVariable Long id, AuthUser authUser) {
        UserDetailResponse getUserDetail = userService.getUserDetail(id);
        return getUserDetail;
    }

    @PostMapping("/user/getAuthority")
    public Integer getAuthority(AuthUser authUser) {
        return userService.getAuthority(authUser.getUserId());
    }






}
