package com.example.demo.service;

import com.example.demo.entity.Center;
import com.example.demo.entity.CenterMember;
import com.example.demo.entity.CenterTrainer;
import com.example.demo.mapper.CenterMapper;
import com.example.demo.request.CenterRequest;
import com.example.demo.response.CenterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


        //db에서 centerFile 지우기
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
        for(Center na: nameAndId){
            System.out.println(na.getId());
        }

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
}






