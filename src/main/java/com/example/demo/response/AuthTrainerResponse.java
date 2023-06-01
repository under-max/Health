package com.example.demo.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
public class AuthTrainerResponse {
    private List<UserListResponse> list;
    private Boolean isMember;
    @Builder
    public AuthTrainerResponse(List<UserListResponse> list, Boolean isMember) {
        this.list = list;
        this.isMember = isMember;
    }
}
