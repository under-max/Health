package com.example.demo.service;

import com.example.demo.entity.Center;
import com.example.demo.entity.CenterComment;
import com.example.demo.entity.CenterMember;
import com.example.demo.entity.CenterTrainer;
import com.example.demo.mapper.CenterMapper;
import com.example.demo.request.CenterRequest;
import com.example.demo.response.CenterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)

public class CenterService {

    private final CenterMapper centerMapper;

    @Autowired
    private S3Client s3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    public CenterService(CenterMapper centerMapper){
        this.centerMapper = centerMapper;
    }

    public Integer infoInsert(CenterRequest centerRequest, MultipartFile[] centerImg) throws Exception{

        Center center = Center
                .builder()
                .name(centerRequest.getName())
                .address(centerRequest.getAddress())
                .info(centerRequest.getInfo())
                .phoneNumber(centerRequest.getPhoneNumber())
                .build();

        centerMapper.infoInsert(center);
        Integer Id = center.getId();

        //aws 서버 올리기
        for(MultipartFile file : centerImg){
            if(file.getSize() > 0){
                String objectKey = "health/" + Id + "/" + file.getOriginalFilename();
                PutObjectRequest por = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build();

                RequestBody rb = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
                s3.putObject(por,rb);

                //db 정보저장 하는 로직
                centerMapper.fileInfoInsert(Id, file.getOriginalFilename());
            }
        }
        return Id;
    }


    public List<CenterResponse> getByCenterName(String centerName) {

        List<Center> center = centerMapper.findByCenterName(centerName); //center정보

        List<CenterResponse> centerResponse = new ArrayList<>();

        for(Center list : center){
            CenterResponse centerResponseList = new CenterResponse(list);
            centerResponse.add(centerResponseList);
        }

        return centerResponse;
    }


    //아이디로 삭제 로직
    public void deleteById(Integer id) {


        //trainer사진 aws에서 삭제
        List<CenterTrainer> centerTrainers = centerMapper.findTrainerFileByCneterId(id); //리스트 들고옴
        System.out.println("aaa");
        for (CenterTrainer centerTrainer : centerTrainers){
            String objectKey = "health/" + centerTrainer.getCenterId() + "/" + centerTrainer.getTrainerId() + "/" + centerTrainer.getFileName();
            DeleteObjectRequest dor = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            s3.deleteObject(dor);
        }

        //db에서 trainerFile table에서 삭제

        centerMapper.deleteTrainerFileByCenterId(id);
        //db에서 trainer 정보 삭제

        centerMapper.deleteTrainerByCenterId(id);

        //id로 fileName 가져오기
        List<String> fileNames = centerMapper.findFileNameById(id);

        //aws에서 파일 지우기
        for (String fileName : fileNames) {
            String objectKey = "health/" + id + "/" + fileName;
            DeleteObjectRequest dor = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            s3.deleteObject(dor);
        }


//       db에서 centerFile 지우기
        centerMapper.deleteCenterFileById(id);


        //db에서 center 지우기
        centerMapper.deleteCenterById(id);

    }


    // 센터 수정
    public void modifyCenter(CenterRequest centerRequest, List<String> removeImg, MultipartFile[] addFile) throws Exception{

        Center center = Center.builder()
                .id(centerRequest.getId())
                .name(centerRequest.getName())
                .address(centerRequest.getAddress())
                .info(centerRequest.getInfo())
                .phoneNumber(centerRequest.getPhoneNumber())
                .build();

        // 클릭된 fileName db에서 삭제

        if (removeImg != null && !removeImg.isEmpty()) {
            for (String fileName : removeImg) {
                // 클릭된 fileName aws에서 삭제
                String objectKey = "health/" + center.getId() + "/" + fileName;
                DeleteObjectRequest dor = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();
                s3.deleteObject(dor);

                // db 테이블에서 삭제
                centerMapper.deleteCenterFileByIdAndFileName(center.getId(), fileName);
            }
        }


        if(addFile != null){
            for (MultipartFile newFile : addFile) {
                if (newFile.getSize() > 0) {
                    // 추가된 filename db에 추가
                    centerMapper.insertFileName(center.getId(), newFile.getOriginalFilename());

                    // 추가된 fileName aws에 추가
                    String objectKey = "health/" + center.getId() + "/" + newFile.getOriginalFilename();
                    PutObjectRequest por = PutObjectRequest.builder()
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .bucket(bucketName)
                            .key(objectKey)
                            .build();
                    RequestBody rb = RequestBody.fromInputStream(newFile.getInputStream(), newFile.getSize());
                    s3.putObject(por, rb);
                }
            }
        }

        //업데이트된 Center 정보 처리
        centerMapper.updateCenterById(center);

    }




    // 트레이너 정보처리

    public List<CenterResponse> getCenterNameForTrainer(String centerName) {
        List<Center> nameAndId = centerMapper.getCenterNameForTrainer(centerName);

        List<CenterResponse> response = new ArrayList<>();
        for(Center naId: nameAndId){
            response.add(new CenterResponse(naId.getId(), naId.getName()));
        }
        return response;
    }
    //멤버 정보 가져오기
    public List<CenterMember> getMemberNameForTrainer(String memberName) {
        List<CenterMember> centerMembers = centerMapper.getMemberInfoByMemberName(memberName);

        return centerMembers;
    }

    // 트레이너 정보 기입처리
    public void inputInfoTrainer(CenterTrainer centerTrainer, CenterMember centerMember, MultipartFile[] trainerImg) throws Exception{


        // 트레이너 정보 입력후 트레이너파일 테이블을 위한 id 회신

        centerMapper.inputInfoTrainerTable(centerTrainer);
        Long trainerId = centerTrainer.getId();


        //aws 트레이너 사진 처리 (사진 /health/centerId/trainer이름/파일명)
        if(trainerImg.length != 0){
            for(MultipartFile file : trainerImg){
                if(file.getSize() > 0){
                    String objectKey = "health/" + centerTrainer.getCenterId() + "/" + trainerId + "/" + file.getOriginalFilename();
                    PutObjectRequest por = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build();

                    RequestBody rb = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
                    s3.putObject(por,rb);

                    // db에 트레이너 img 입력처리
                    centerMapper.inputTrainerFile(trainerId, centerTrainer.getCenterId(), file.getOriginalFilename());
                }
            }
        }


        // Member에 centerId, trainerId, authority update

        centerMapper.updateMemberAuthority(centerMember.getCenterId(),centerMember.getAuthority(),centerMember.getId());

    }


    public List<CenterResponse> searchCenterNameOrTrainerName(String searchName) {

            //검색요청이 centerName 기준일떄 (join을 위해서 일단 centerId필요 Center테이블 검색, 전체 테이블 CENTER, CENTERFILE, TRAINER, MEMBER)
            List<Center> centers = centerMapper.searchByCenterName(searchName);

            List<CenterResponse> centerResponse = new ArrayList<>();
            for (Center center: centers){
                centerResponse.add(new CenterResponse(center));
            }

            return centerResponse;
    }

    //Join 통한 필요테이블 소환
    public List<CenterTrainer> searchJoinTrainerCenterName(String centerName) {

        List<CenterTrainer> list = centerMapper.searchJoinTrainerCenterName(centerName);
        System.out.println(list);

        return list;
    }

    //Trainer 정보 삭제 (aws삭제, db삭제(1. trainerFile테이블 삭제, 2. trainer테이블 삭제, 3. member테이블(centerId = null, authority = 1) ))
    public void deleteTrainer(CenterTrainer data) {

        // aws 삭제
        String objectKey = "health/" + data.getCenterId() + "/"+ data.getId() +"/" + data.getFileName();
        DeleteObjectRequest dor = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        s3.deleteObject(dor);

        //db삭제
        //1.TrainerFile 테이블 삭제
        centerMapper.deleteTrainerFileByTrainerId(data.getId());

        //2.Trainer 테이블 삭제
        centerMapper.deleteTrainerByTrainerId(data.getId());

        // 3.member 테이블에서 centerId = null, authority = 1 로 업데이트, WHERE 조건 memberID 필요
        Long authority = 1L;
        String centerId = null;
        centerMapper.updateMemberCenterIdAndAuthority(centerId, authority, data.getMemberId());
    }

    public void modifyTrainer(MultipartFile[] trainerImg, Long trainerId, String removeImg, String modifyInfo, Long centerId, String modifyInfoDetail) throws Exception{

        //트레이너 이미지는 하나만 등록해야함 remove가 있다는건 등록할 trainerImg 도 있다는 뜻
        // 삭제할 파일명 있을수도 없을수도 있음
        if(removeImg != null && !removeImg.equals("")){
            System.out.println("파일명 있으니까 지움");
            //aws 삭제로직
            String objectKey = "health/" + centerId + "/"+ trainerId +"/" + removeImg;
            DeleteObjectRequest dor = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            s3.deleteObject(dor);
            //db TrainerFIle 삭제 로직

            centerMapper.deleteTrainerFileByTrainerId(trainerId);
        }
        // 추가할 파일명 있을수도 없을 수도 있음

        if(trainerImg != null){
            for (MultipartFile file : trainerImg){
                if(file.getSize() > 0){
                    String objectKey = "health/" + centerId + "/" + trainerId + "/" + file.getOriginalFilename();
                    PutObjectRequest por = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build();

                    RequestBody rb = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
                    s3.putObject(por,rb);

                    //db TrainerFile 추가 로직
                    centerMapper.inputTrainerFile(trainerId, centerId, file.getOriginalFilename());
                }
            }
       }

        //trainer info 업데이트 로직(trainerId로 검색, 바꿀 내용 modifyInfo)
        centerMapper.modifyTrainerByTrainerId(modifyInfo, trainerId, modifyInfoDetail);
    }


    public Map<String, Object> getAllCenterList(Long limit, Long offset) {
        List<Center> centers = centerMapper.getAllCenterList(limit, offset);
        List<CenterResponse> response = new ArrayList<>();

        Long centerCount = centerMapper.getAllCenterCount();

        for (Center center: centers){
            response.add(new CenterResponse(center));
        }

        Map<String, Object> responses = new HashMap<>();
        responses.put("center", response);
        responses.put("centerCount", centerCount);
        return responses;
    }



    public CenterResponse getCenterDetailByCenterId(Long centerId) {

        CenterResponse responses = new CenterResponse(centerMapper.getCenterDetailByCenterId(centerId));
        return responses;
    }


    public CenterTrainer getTrainerDetailByMemberId(Long trainerId) {

        return centerMapper.getTrainerDetailByMemberId(trainerId);
    }



    public CenterMember getMemberInfoByMemberId(Long userId) {
        CenterMember centerMember = centerMapper.getMemberInfoByMemberId(userId);
        return centerMember;
    }


    public void insertCenterComment(CenterComment centerComment) {
        centerMapper.insertCenterComment(centerComment);
    }


    public Map<String, Object> getCenterComment(Long centerId, Long offset) {
        Map<String, Object> commentList = new HashMap<>();
        List<CenterComment> centerList = centerMapper.getCenterComment(centerId, offset);
        commentList.put("centerCommentData", centerList);

        Long count = centerMapper.getCenterCommentCount();
        commentList.put("centerCommentCount",count);

        return commentList;
    }


    public void deleteCenterCommentById(Long id) {
        centerMapper.deleteCenterCommentById(id);
    }



    public void modifyCenterCommentById(CenterComment centerComment) {
        centerMapper.modifyCenterCommentById(centerComment);
    }



    public Map<String, Object> centerSearchByType(String searchType, String searchValue, Long offset) {

        List<Center> centers = centerMapper.centerSearchByType(searchType, searchValue, offset);
        Long searchCount = centerMapper.centerSearchByTypeCount(searchType, searchValue);
        List<CenterResponse> centerResponse = new ArrayList<>();

        for(Center center: centers){
            centerResponse.add(new CenterResponse(center));

        }

        Map<String, Object> responses = new HashMap<>();

        responses.put("centerSearchList", centerResponse);
        responses.put("searchCount", searchCount);

        return responses;
    }


    public Map<String, Object> checkTrainer(Long memberId, Long centerId) {
        Long check = centerMapper.checkTrainer(memberId, centerId);
        Map<String, Object> trainerChecker = new HashMap<>();
        String message;
        Boolean checker;
        if(check == null){
            message = "등록 가능한 트레이너 입니다.";
            checker = true;
            trainerChecker.put("message", message);
            trainerChecker.put("checker", checker);
            return trainerChecker;
        }else {
            message = "이미 해당 센터에 등록된 트레이너 입니다.";
            checker = false;
            trainerChecker.put("message", message);
            trainerChecker.put("checker", checker);
            return trainerChecker;
        }
    }


}






