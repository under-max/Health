package com.example.demo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserCreate {
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
    public UserCreate(Long id, String nickName, String email, String password, String name, String address, Date birthDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
    }

    @Builder
    public UserCreate(String email, String password, String name, String address, Date birthDate) {
        this.password = password;
        this.nickName = nickName;
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
    }
}
