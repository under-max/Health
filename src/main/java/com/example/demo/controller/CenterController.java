package com.example.demo.controller;

import com.example.demo.config.AuthResolver;
import com.example.demo.entity.*;
import com.example.demo.request.CenterRequest;
import com.example.demo.response.CenterResponse;
import com.example.demo.service.CenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
                                 @RequestParam("infoDetail") String infoDetail,
                                 @RequestParam("name") String name,
                                 @RequestParam("nickName") String nickName,
                                 @RequestParam("authority") Long authority,
                                 @RequestParam("trainerImg") MultipartFile[] trainerImg) throws Exception{

        CenterTrainer centerTrainer = CenterTrainer.builder()
                .memberId(memberId)
                .centerId(centerId)
                .info(info)
                .infoDetail(infoDetail)
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

    //트레이너 정보수정 진행 검색창
    @GetMapping("/center/searchCenterName")
    public List<CenterResponse> centerSearchCenterNameOrTrainerName(@RequestParam("searchName") String searchName){               ;

        List<CenterResponse> centerResponses = centerService.searchCenterNameOrTrainerName(searchName);

        return centerResponses;
    }

    //트레이너 정보수정 join 테이블 처리
    @GetMapping("/center/modifyTrainerSearch")
    public List<CenterTrainer> centerTrainerModifyResult(@RequestParam("centerName") String centerName){

        List<CenterTrainer> list = centerService.searchJoinTrainerCenterName(centerName);

        return list;
    }

    //트레이너 삭제
    @DeleteMapping("/center/deleteTrainer")
    public void centerDeleteTrainer(@RequestBody CenterTrainer data){
        centerService.deleteTrainer(data);
        System.out.println(data);
    }

    //트레이너 정보 수정
    @PutMapping("/center/trainerModify")
    public void centerTrainerModify(@RequestParam(value = "trainerImg", required = false) MultipartFile[] trainerImg,
                                    @RequestParam("trainerId") Long trainerId,
                                    @RequestParam("centerId") Long centerId,
                                    @RequestParam("modifyInfoDetail") String modifyInfoDetail,
                                    @RequestParam(value = "removeImg", required = false) String removeImg,
                                    @RequestParam("modifyInfo") String modifyInfo) throws Exception{


        centerService.modifyTrainer(trainerImg, trainerId, removeImg, modifyInfo, centerId, modifyInfoDetail);

    }


    @GetMapping("/center/allCenterList")
    public Map<String, Object> allCenterList(@RequestParam("limit") Long limit, @RequestParam("offset") Long offset){

        Map<String, Object> responses = centerService.getAllCenterList(limit,offset);

        return responses;
    }


    @GetMapping("/center/{id}")
    public CenterResponse centerDetail(@RequestParam("centerId") Long centerId){
        return centerService.getCenterDetailByCenterId(centerId);
    }


    @GetMapping("center/getTrainerDetail")
    public CenterTrainer getTrainerDetailByMemberId(@RequestParam("trainerId") Long trainerId){
        return centerService.getTrainerDetailByMemberId(trainerId);
    }

   @GetMapping("center/checkToken")
    public CenterMember checkToken(AuthUser authUser){

        return centerService.getMemberInfoByMemberId(authUser.getUserId());
    }


    @PostMapping("center/insertCenterComment")
    public void insertCenterComment(@RequestBody CenterComment centerComment){
        centerService.insertCenterComment(centerComment);
    }

    @GetMapping("center/getCenterComment")
    public Map<String, Object> getCenterComment(@RequestParam("centerId") Long centerId,
                                                @RequestParam("offset") Long offset){
        Map<String,Object> list = centerService.getCenterComment(centerId, offset);
        return list;
    }

    @DeleteMapping("/center/deleteCenterComment")
    public void deleteCenterComment(@RequestParam("id") Long id){
        System.out.println(id);
        centerService.deleteCenterCommentById(id);
    }

    @PutMapping("/center/modifyCenterComment")
    public void modifyCenterComment(@RequestBody CenterComment centerComment){
        System.out.println(centerComment);
        centerService.modifyCenterCommentById(centerComment);
    }

    @GetMapping("/center/centerSearchByType")
    public Map<String, Object> centerSearchByType(@RequestParam("searchType") String searchType,
                                   @RequestParam("searchValue") String searchValue,
                                   @RequestParam("offset") Long offset){
        Map<String, Object> responses = centerService.centerSearchByType(searchType, searchValue, offset);
        return responses;
    }


    @GetMapping("/center/checkTrainer")
    public Map<String, Object> checkTrainer(@RequestParam("memberId") Long memberId,
                             @RequestParam("centerId") Long centerId){
        return centerService.checkTrainer(memberId, centerId);
    }
}
