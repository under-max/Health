package com.example.demo.controller;

import com.example.demo.entity.Center;
import com.example.demo.entity.CenterMember;
import com.example.demo.entity.CenterTrainer;
import com.example.demo.request.CenterRequest;
import com.example.demo.response.CenterResponse;
import com.example.demo.service.CenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
public class CenterController {


    private final CenterService centerService;

    @Autowired
    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

    //create
    @PostMapping(value = "center/infoInsert", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String centerInputImg(@RequestParam("centerImg") MultipartFile[] centerImg,
                                 @RequestParam("name") String name,
                                 @RequestParam("address") String address,
                                 @RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("info") String info) throws Exception{

        CenterRequest centerRequest = CenterRequest.builder()
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .info(info)
                .build();

       centerService.infoInsert(centerRequest, centerImg);

        return "이미지 완료";
    }

    //센터검색
    @GetMapping("/center/searchname")
    public List<CenterResponse> CenterSearch(@RequestParam("centerName") String centerName){


        List<CenterResponse> responses = centerService.getByCenterName(centerName);

        return responses;
    }


    //센터 삭제
    @DeleteMapping("/center/delete")
    public void deleteCenter(@RequestBody CenterRequest centerRequest){
        centerService.deleteById(centerRequest.getId());
    }


    @PutMapping("/center/modify")
    public void modifyCenter(@RequestParam("id")Integer id,
                             @RequestParam("name") String name,
                             @RequestParam("address") String address,
                             @RequestParam("info") String info,
                             @RequestParam("phoneNumber") String phoneNumber,
                             @RequestParam(value = "removeImg", required = false) List<String> removeImg,
                             @RequestParam(value = "addImg", required = false) MultipartFile[] addFile) throws Exception{

        CenterRequest centerRequest = CenterRequest.builder()
                .id(id)
                .name(name)
                .address(address)
                .info(info)
                .phoneNumber(phoneNumber)
                .build();


        centerService.modifyCenter(centerRequest,removeImg,addFile);
    }

    //트레이너 정보처리 로직
    //CenterName으로 리스트 Get
    @GetMapping("/center/centerNameInfo")
    public List<CenterResponse> getCenterNameForTrainer(@RequestParam("centerName") String centerName){

        List<CenterResponse> centerResponses = centerService.getCenterNameForTrainer(centerName);

        return centerResponses;
    }

    //회원이름으로 리스트 Get
    @GetMapping("/center/membersearch")
    public List<CenterMember> getMemberNameForTrainer(@RequestParam("memberName") String memberName){

        List<CenterMember> centerMembers = centerService.getMemberNameForTrainer(memberName);
        return centerMembers;
    }

    //트레이너 정보 create
    @PostMapping("/center/inputInfoTrainer")
    public void inputInfoTrainer(@RequestParam("memberId") Long memberId,
                                 @RequestParam("centerId") Long centerId,
                                 @RequestParam("info") String info,
                                 @RequestParam("name") String name,
                                 @RequestParam("nickName") String nickName,
                                 @RequestParam("authority") Long authority,
                                 @RequestParam("trainerImg") MultipartFile[] trainerImg) throws Exception{

        CenterTrainer centerTrainer = CenterTrainer.builder()
                .memberId(memberId)
                .centerId(centerId)
                .info(info)
                .name(name)
                .nickName(nickName)
                .build();

        CenterMember centerMember = CenterMember.builder()
                .id(memberId)
                .centerId(centerId)
                .authority(authority)
                .build();

        centerService.inputInfoTrainer(centerTrainer, centerMember, trainerImg);

    }


}
