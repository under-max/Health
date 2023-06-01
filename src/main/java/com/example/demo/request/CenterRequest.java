package com.example.demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@NoArgsConstructor
public class CenterRequest {
    private Integer id;
    @NotBlank(message = "이름이 없습니다")
    private String name;

    @NotBlank(message = "주소가 없습니다")
    private String address;

    @NotBlank(message = "매장정보가 없습니다")
    private String info;

    @NotBlank(message = "전화번호가 없습니다")
    private String phoneNumber;

    private List<String> fileName;

    // Member 필요 테이블 memberId, name, nickName, authority

    // Trainer 필요테이블 info(직접입력), name(memberTable), nickName(memberTable), memberId(memberTable), centerId(memberTable)

    @Builder
    public CenterRequest(Integer id, String name, String address, String info, String phoneNumber, List<String> fileName){
        this.id = id;
        this.name = name;
        this.address = address;
        this.info = info;
        this.phoneNumber = phoneNumber;
        this.fileName = fileName;
    }
}
