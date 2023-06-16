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


    // Join data 들고오는 용도로
    private String centerName;

    private String fileName;


    @Builder
    public CenterTrainer(Long id, Long memberId, Long centerId, String info, String name, String nickName, String centerName, String fileName){
        this.id = id;
        this.memberId = memberId;
        this.centerId = centerId;
        this.info = info;
        this.name = name;
        this.nickName = nickName;

        this.centerName = centerName;
        this.fileName = fileName;
    }

}
