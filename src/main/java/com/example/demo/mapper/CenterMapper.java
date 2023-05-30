package com.example.demo.mapper;

import com.example.demo.entity.Center;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CenterMapper {

    //center 정보 입력 mapper

    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void infoInsert(Center center);


    @Insert("""
            INSERT INTO CENTERFILE (centerId, fileName)
            VALUES
                  (#{centerId}, #{fileName})
            """)
    void fileInfoInsert(@Param("centerId") Integer centerId,@Param("fileName") String fileName);


    @Select("""
            <script>
            <bind name="pattern" value="'%' + centerName + '%'"/>
            SELECT c.id, c.name, c.address, c.info, c.phoneNumber, f.fileName
            FROM CENTER c
            LEFT JOIN CENTERFILE f ON c.id = f.centerId
            WHERE c.name LIKE #{pattern}
            </script>
            """)
    @ResultMap("findByCenterName")
    List<Center> findByCenterName(@Param("centerName") String centerName);



}
