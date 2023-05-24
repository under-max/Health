package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.User;
import com.example.demo.request.UserDelete;
import com.example.demo.request.UserCreate;
import com.example.demo.request.UserEdit;
import com.example.demo.response.UserDetailResponse;
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

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable int id, AuthUser authUser) {
        userService.delete(UserDelete.builder()
                .authedUserId(authUser.getUserId())
                .build());
    }

    @GetMapping("/test/lists")
    public List<User> list() {
        List<User> listAll = userService.listAllTest();
        return listAll;
    }

    @GetMapping("/test/user/{id}")
    public UserDetailResponse UserDetail(@PathVariable Long id) {
        UserDetailResponse detailTest = userService.getTest(id);
        log.info("호출");
        return detailTest;
    }

//    @GetMapping("/test/lists")
//    public User list() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("강백호");
//        return user;
//    }

    //    @GetMapping("/test/lists")
//    public Map<Long, Object> list() {
//        Map<Long, Object> map = Map.of(1L, Map.of("id", 1L, "name", "강백호"));
//        return map;
//    }



}
