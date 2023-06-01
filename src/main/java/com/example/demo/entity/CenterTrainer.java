package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CenterTrainer {

    private Long id;

    private Long memberId;

    private Long centerId;

    private String info;

    private String name;

    private String nickName;

    @Builder
    public CenterTrainer(Long id, Long memberId, Long centerId, String info, String name, String nickName){
        this.id = id;
        this.memberId = memberId;
        this.centerId = centerId;
        this.info = info;
        this.name = name;
        this.nickName = nickName;
    }

}
