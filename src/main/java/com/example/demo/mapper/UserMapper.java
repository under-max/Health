package com.example.demo.mapper;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Trainer;
import com.example.demo.entity.User;
import com.example.demo.request.Login;
import com.example.demo.request.UserEdit;
import com.example.demo.response.CenterDetailResponse;
import com.example.demo.response.CenterResponse;
import com.example.demo.response.UserDetailResponse;
import com.example.demo.response.UserListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("""
            SELECT * FROM MEMBER
            WHERE email = #{email}
            """)
    Optional<User> findByEmail(String email);
    @Select("""
            SELECT * FROM MEMBER
            WHERE nickName = #{nickName}
            """)
    Optional<User> findByNickName(String nickName);
    @Select("""
            SELECT * FROM MEMBER
            WHERE id = #{id}
            """)
    Optional<User> findById(Long authedUserId);

    @Select("""
            SELECT * FROM MEMBER
            WHERE email = #{email} and password = #{password}
            """)
    Optional<User> findByEmailAndPassword(Login login);

    @Insert("""
            INSERT INTO MEMBER(email, password, nickName, name, address, birthDate )
            VALUES(#{email}, #{password}, #{nickName}, #{name}, #{address}, #{birthDate})
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void save(User user);
    @Update("""
            UPDATE MEMBER SET
                password = #{password},
                nickName = #{nickName},
                name = #{name},
                address = #{address},
                birthDate = #{birthDate}
            WHERE ID = #{userId}
            """)
    void update(UserEdit userEdit);
    @Delete("""
            DELETE FROM MEMBER
            WHERE id = #{id}
            """)
    void delete(Long id);
    @Delete("""
            DELETE FROM MEMBER
            """)
    void deleteAll();

    @Select("""
            SELECT * FROM MEMBER 
            ORDER BY 1 DESC
            LIMIT 30 OFFSET ${(page > 0) ? (page-1) * 30 : 0}
            """)
    List<User> findAll(int page);


    @Select("""
            SELECT m.id, m.name, m.birthDate, t.name as trainerName,
                   ms.startDate, ms.endDate, ms.remainingPT, 
                   m.isInCenter
            FROM MEMBER m
            LEFT JOIN
            	TRAINER t ON m.trainerId = t.id
            LEFT JOIN
            	MEMBERSHIP ms ON m.id = ms.memberId
            WHERE m.authority = 1;
            """)
    List<UserListResponse> selectById();

    @Select("""
            SELECT
                m.id
                , m.name
                , m.birthDate
                , c.id as centerId
                , c.name as centerName
                , c.address as centerAddress
                , t.memberId as trainerId
                , t.name as trainerName
                , ms.startDate
                , ms.endDate
                , ms.remainingPT
                , m.isInCenter
                , m.authority
            FROM MEMBER m
            LEFT JOIN
            	CENTER c ON m.centerId = c.id
            LEFT JOIN
            	TRAINER t ON m.trainerId = t.id
            LEFT JOIN
            	MEMBERSHIP ms ON m.id = ms.memberId
            WHERE m.id = #{id}  ;
            """)
    UserDetailResponse getUserDetail(Long id);

    @Insert("""
            UPDATE MEMBER SET
                password = #{password},
                nickName = #{nickName},
                name = #{name},
                address = #{address},
                birthDate = #{birthDate}
            WHERE ID = #{id}
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void saveSocial(User user);


    @Select("""
            SELECT
                m.id
                , m.name
                , m.birthDate
                , c.id as centerId
                , c.name as centerName
                , c.address as centerAddress
                , t.name as trainerName
                , ms.startDate
                , ms.endDate
                , ms.remainingPT
                , m.isInCenter
                , m.authority
            FROM MEMBER m
            LEFT JOIN
            	CENTER c ON m.centerId = c.id
            LEFT JOIN
            	TRAINER t ON m.trainerId = t.id
            LEFT JOIN
            	MEMBERSHIP ms ON m.id = ms.memberId
            WHERE m.id = #{id};       
            """)
    UserListResponse findByAuthUserId(Long id);

    @Select("""
            SELECT * FROM TRAINER 
            WHERE memberId = #{id}
            """)
    Trainer getTrainerId(Long id);

    @Select("""
            SELECT m.id, m.name, m.birthDate, t.name as trainerName,
                   ms.startDate, ms.endDate, ms.remainingPT, m.authority,
                   m.isInCenter
            FROM MEMBER m
            LEFT JOIN
            	TRAINER t ON m.trainerId = t.id
            LEFT JOIN
            	MEMBERSHIP ms ON m.id = ms.memberId
            WHERE t.id = #{trainerId}
            """)
    List<UserListResponse> findByAllAuthUserId(Integer trainerId);

    @Select("""
            SELECT authority FROM MEMBER
            WHERE id = #{id}
            """)
    Integer getAuthority(Long id);


//    여기 아래 수정
    @Select("""
            SELECT c.id, c.name, c.address, c.info, ROUND(AVG(cc.star),2) star, c.phoneNumber  FROM CENTER c
            JOIN CENTERCOMMENT cc ON c.id = cc.centerId
            JOIN MEMBER m ON c.id  = m.centerId
            WHERE m.id = #{id};
            """)
    CenterDetailResponse getCenter(Long id);


}
