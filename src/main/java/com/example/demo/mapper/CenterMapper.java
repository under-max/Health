package com.example.demo.mapper;

import com.example.demo.entity.CenterEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CenterMapper {

    //center 정보 입력 mapper

    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void infoInsert(CenterEntity centerEntity);


    @Insert("""
            INSERT INTO CENTERFILE (centerId, fileName)
            VALUES
                  (#{centerId}, #{fileName})
            """)
    void fileInfoInsert(@Param("centerId") Integer centerId,@Param("fileName") String fileName);



}
