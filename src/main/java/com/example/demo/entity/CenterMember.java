package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CenterMember {

    private Long id;

    private Long centerId;

    private Long trainerId;

    private String name;

    private String email;

    private String nickName;

    private Long authority;

    @Builder
    public CenterMember(Long id, String name, String nickName, Long authority, String email, Long centerId, Long trainerId){
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.authority = authority;
        this.email = email;
        this.centerId = centerId;
        this.trainerId = trainerId;
    }

}
