package com.example.demo.mapper;

import com.example.demo.entity.Center;
import com.example.demo.entity.CenterComment;
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

    //트레이너 수정 삭제 센터명 검색
    @Select("""
            <script>
            <bind name="pattern" value="'%' + searchName + '%'"/>
            SELECT id, name
            FROM CENTER
            WHERE name LIKE #{pattern}
            </script>
            """)
    List<Center> searchByCenterName(String searchName);
    //Trainer Modify창을 위한 정보 출력
    @Select("""
            <script>            
            SELECT DISTINCT c.name AS "centerName",
                   c.id AS "centerId",
                   t.name AS "name",
                   t.memberId AS "memberId",
                   t.info AS "info",
                   t.id AS id,
                   tr.fileName AS "fileName"                        
            FROM CENTER c
            JOIN TRAINER t ON c.id = t.centerId
            JOIN TRAINERFILE tr ON t.id = tr.trainerId
            JOIN MEMBER m ON c.id = m.centerId
            WHERE c.name = #{centerName}        
            </script>
            """)
    List<CenterTrainer> searchJoinTrainerCenterName(String centerName);

    @Delete("""
            DELETE FROM TRAINERFILE
            WHERE trainerId = #{id}
            """)
    void deleteTrainerFileByTrainerId(Long id);


    @Delete("""
            DELETE FROM TRAINER
            WHERE id = #{id}
            """)
    void deleteTrainerByTrainerId(Long id);

    @Update("""
            UPDATE MEMBER
            SET centerId = #{centerId}, authority = #{authority}
            WHERE id = #{memberId}
            """)
    void updateMemberCenterIdAndAuthority(@Param("centerId") String centerId,@Param("authority") Long authority,@Param("memberId") Long memberId);


    @Update("""
            UPDATE TRAINER
            SET info = #{modifyInfo}
            WHERE id = #{trainerId}
            """)
    void modifyTrainerByTrainerId(@Param("modifyInfo") String modifyInfo,@Param("trainerId") Long trainerId);


    @Select("""
            <script>
            SELECT c.id, c.name, c.address, c.info, c.phoneNumber, cf.fileName, COALESCE(cc.star, 0) AS star
            FROM CENTER c
            LEFT JOIN (
                SELECT centerId, MAX(fileName) AS fileName
                FROM CENTERFILE
                GROUP BY centerId
            ) cf ON c.id = cf.centerId
            LEFT JOIN (
                SELECT centerId, avg(star) AS star
                FROM CENTERCOMMENT
                GROUP BY centerId
            ) cc ON c.id = cc.centerId
            ORDER BY c.name ASC
            LIMIT #{limit} OFFSET #{offset}
            </script>
            """)
    @ResultMap("getAllCenterList")
    List<Center> getAllCenterList(@Param("limit") Long limit,@Param("offset") Long offset);

    @Select("""
            SELECT COUNT(*)
            FROM CENTER c
            RIGHT JOIN (
                SELECT centerId, MAX(fileName) AS fileName
                FROM CENTERFILE
                GROUP BY centerId
            ) f ON c.id = f.centerId
            ORDER BY c.name ASC
            """)
    Long getAllCenterCount();

    @Select("""
            <script>
            SELECT c.id, c.name, c.address, c.info, c.phoneNumber, t.name as trainerName, f.fileName, t.id as trainerId
            FROM CENTER c
            LEFT JOIN TRAINER t ON c.id = t.centerId
            LEFT JOIN CENTERFILE f ON c.id = f.centerId
            WHERE c.id = #{id}
            ORDER BY c.name ASC;     
            </script>
            """)
    @ResultMap("getCenterDetailByCenterId")
    Center getCenterDetailByCenterId(Long id);


    @Select("""
            <script>
            SELECT t.info, t.name, t.nickname, f.centerId, f.fileName, t.id
            FROM TRAINER t
            LEFT JOIN TRAINERFILE f ON t.id = f.trainerId
            WHERE trainerId = #{trainerId};
            </script>
            """)
    CenterTrainer getTrainerDetailByMemberId(@Param("trainerId") Long trainerId);

    @Select("""
            SELECT id, centerId, name, nickName, authority, email
            FROM MEMBER
            WHERE id = #{userId}
            """)
    CenterMember getMemberInfoByMemberId(@Param("userId") Long userId);


    @Insert("""
            INSERT INTO CENTERCOMMENT (star, comment, memberId, centerId)
            VALUES (#{star}, #{comment}, #{memberId}, #{centerId})
            """)
    void insertCenterComment(CenterComment centerComment);

    @Select("""
            SELECT c.id, c.star, c.comment, c.memberId, c.inserted, m.nickName
            FROM CENTERCOMMENT c
            JOIN MEMBER m ON c.memberId = m.id
            WHERE c.centerId = #{centerId}
            ORDER BY c.inserted DESC
            LIMIT 5 OFFSET #{offset};
            """)
    List<CenterComment> getCenterComment(@Param("centerId") Long centerId, @Param("offset") Long offset);

    @Select("""
            SELECT COUNT(*)
            FROM CENTERCOMMENT
            """)
    Long getCenterCommentCount();

    @Delete("""
            DELETE FROM CENTERCOMMENT
            WHERE id = #{id}
            """)
    void deleteCenterCommentById(Long id);


    @Update("""
            UPDATE CENTERCOMMENT
            SET comment=#{comment}, star=#{star}
            WHERE id = #{id}
            """)
    void modifyCenterCommentById(CenterComment centerComment);


    @Select("""
            <script>
            <bind name="pattern" value="'%' + searchValue + '%'"/>
            SELECT c.id, c.name, c.address, c.info, c.phoneNumber, cf.fileName, COALESCE(cc.star, 0) AS star
            FROM CENTER c
            LEFT JOIN (
                SELECT centerId, MAX(fileName) AS fileName
                FROM CENTERFILE
                GROUP BY centerId
            ) cf ON c.id = cf.centerId
            LEFT JOIN (
                SELECT centerId, avg(star) AS star
                FROM CENTERCOMMENT
                GROUP BY centerId
            ) cc ON c.id = cc.centerId
            WHERE
            <if test="searchType == 'centerName'">
                c.name LIKE #{pattern}
            </if>
            <if test="searchType == 'address'">
                c.address LIKE #{pattern}
            </if>
            ORDER BY c.name ASC
            LIMIT 3 OFFSET #{offset}
            </script>
            """)
    @ResultMap("searchCenterListByType")
    List<Center> centerSearchByType(@Param("searchType") String searchType,@Param("searchValue") String searchValue,@Param("offset") Long offset);


    @Select("""
            <script>
            <bind name="pattern" value="'%' + searchValue + '%'"/>
            SELECT COUNT(*)
            FROM CENTER c
            LEFT JOIN (
                SELECT centerId, MAX(fileName) AS fileName
                FROM CENTERFILE
                GROUP BY centerId
            ) cf ON c.id = cf.centerId
            LEFT JOIN (
                SELECT centerId, avg(star) AS star
                FROM CENTERCOMMENT
                GROUP BY centerId
            ) cc ON c.id = cc.centerId
            WHERE
            <if test="searchType == 'centerName'">
                c.name LIKE #{pattern}
            </if>
            <if test="searchType == 'address'">
                c.address LIKE #{pattern}
            </if>
            ORDER BY c.name ASC
            </script>
            """)
    Long centerSearchByTypeCount(@Param("searchType") String searchType,@Param("searchValue") String searchValue);
}
