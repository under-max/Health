package com.example.demo.mapper;

import com.example.demo.entity.Center;
import com.example.demo.entity.CenterMember;
import com.example.demo.entity.CenterTrainer;
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


    //centerFile 이름 리스트
    @Select("""
            SELECT fileName FROM CENTERFILE
            WHERE centerId = #{id}
            """)
    List<String> findFileNameById(Integer id);

    //CenterFIle 삭제
    @Delete("""
            DELETE FROM CENTERFILE
            WHERE centerId = #{id}
            """)
    void deleteCenterFileById(Integer id);


    //Center 삭제
    @Delete("""
            DELETE FROM CENTER
            WHERE id = #{id}
            """)
    void deleteCenterById(Integer id);

    @Delete("""
            DELETE FROM CENTERFILE
            WHERE centerId = #{centerId} AND fileName = #{fileName}
            """)
    void deleteCenterFileByIdAndFileName(@Param("centerId") Integer centerId, @Param("fileName") String fileName);

    @Insert("""
            INSERT INTO CENTERFILE (centerId, fileName)
            VALUES
                  (#{centerId}, #{fileName})
            """)
    void insertFileName(@Param("centerId") Integer centerId,@Param("fileName") String fileName);


    @Update("""
            UPDATE CENTER
            SET name = #{name}, address = #{address}, info = #{info}, phoneNumber = #{phoneNumber}
            WHERE id = #{id}
            """)
    void updateCenterById(Center center);




    //트레이너 처리 로직

    @Select("""
            <script>
            <bind name="pattern" value="'%' + centerName + '%'"/>
            SELECT id, name FROM CENTER
            WHERE name like  #{pattern}
            </script>
            """)
    List<Center> getCenterNameForTrainer(String centerName);

    //멤버 이름으로 조회
    @Select("""
            <script>
            <bind name="pattern" value="'%' + memberName + '%'"/>
            SELECT id, authority, name, nickName, email
            FROM MEMBER
            WHERE name like #{pattern}
            </script>
            """)
    List<CenterMember> getMemberInfoByMemberName(String memberName);


    //트레이너 테이블 정보 입력
    @Insert("""
            INSERT INTO TRAINER (memberId, centerId, info, name, nickName)
            VALUES
                  (#{memberId}, #{centerId}, #{info}, #{name}, #{nickName})
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Long inputInfoTrainerTable(CenterTrainer centerTrainer);


    //트레이너파일 테이블 정보 입력
    @Insert("""
            INSERT INTO TRAINERFILE (trainerId, centerId, fileName)
            VALUES
                  (#{trainerId}, #{centerId}, #{fileName})
            """)
    void inputTrainerFile(@Param("trainerId") Long trainerId, @Param("centerId") Long centerId,@Param("fileName") String fileName);
    //멤버테이블 업데이트
    @Update("""
            UPDATE MEMBER
            SET centerId = #{centerId}, authority = #{authority}
            WHERE id = #{id}
            """)
    void updateMemberAuthority(@Param("centerId") Long centerId,@Param("authority") Long authority,@Param("id") Long id);


}
