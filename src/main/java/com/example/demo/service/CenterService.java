package com.example.demo.service;

import com.example.demo.entity.Center;
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
        System.out.println(center);
        centerMapper.infoInsert(center);
        Integer Id = center.getId();
        System.out.println(Id);
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
                System.out.println(Id);
                System.out.println(file.getOriginalFilename());
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
            System.out.println(list);
        }

        return centerResponse;
    }



}






