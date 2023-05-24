package com.example.demo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDelete {
    private Long authedUserId;
    private String password;

    @Builder
    public UserDelete(String password, Long authedUserId) {
        this.password = password;
        this.authedUserId = authedUserId;
    }
}
