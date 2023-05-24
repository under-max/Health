package com.example.demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserEdit {
    private String name;
    private String password;
    private String newPassword;
    private Long userId;

    @Builder
    public UserEdit(Long userId, String password, String name,String newPassword) {
        this.password = password;
        this.name = name;
        this.userId = userId;
        this.newPassword = newPassword;
    }

}
