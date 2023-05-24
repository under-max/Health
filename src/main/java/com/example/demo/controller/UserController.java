package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.User;
import com.example.demo.request.UserDelete;
import com.example.demo.request.UserCreate;
import com.example.demo.request.UserEdit;
import com.example.demo.response.UserResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
                .password(userEdit.getPassword())
                .newPassword(userEdit.getNewPassword())
                .name(userEdit.getName())
                .build());
    }

    @DeleteMapping("/user/{password}")
    public void delete(@PathVariable String password, AuthUser authUser) {
        userService.delete(UserDelete.builder()
                .authedUserId(authUser.getUserId())
                .password(password)
                .build());
    }
}
