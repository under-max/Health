package com.example.demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserEdit {
    private String newPassword;
    private Long userId;
    private Long id;
    @NotBlank(message = "e-mail을 입력하세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickName;
    private String name;
    private String address;
    private Date birthDate;
    @Builder

    public UserEdit(String newPassword, Long userId, Long id, String email, String password, String nickName, String name, String address, Date birthDate) {
        this.newPassword = newPassword;
        this.userId = userId;
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
    }
}
