package com.example.demo.mapper;

import com.example.demo.entity.User;
import com.example.demo.request.Login;
import com.example.demo.request.UserEdit;
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
            WHERE name = #{name}
            """)
    Optional<User> findByName(String name);
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
            INSERT INTO MEMBER(name, email, password)
            VALUES(#{name}, #{email}, #{password})
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void save(User user);
    @Update("""
            <script>
            
            UPDATE MEMBER SET
                name = #{name},
                password = #{password}
            WHERE ID = #{userId}
            
            </script>
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
            SELECT m.id, m.name, m.birthDate,
                   t.name as trainerName, ms.startDate,
                    ms.endDate, ms.remainingPT,
                   m.isInCenter
            FROM MEMBER m
            LEFT JOIN
            	TRAINER t ON m.trainerId = t.id
            LEFT JOIN
            	MEMBERSHIP ms ON m.id = ms.membershipId;
            """)
    List<UserListResponse> findAllTest();

    @Select("""
            SELECT m.id, m.name, m.email, 
            	   m.isInCenter, t.id as TrainerID, t.name as TrainerName, c.name as CenterName, ms.startDate, ms.endDate, ms.remainingPT
            FROM MEMBER m
            LEFT JOIN
            	TRAINER t ON m.trainerId = t.id
            LEFT JOIN
            	CENTER c ON m.centerId = c.id
            LEFT JOIN
            	MEMBERSHIP ms ON ms.membershipId = m.id
            WHERE m.id = #{id}
            """)
    UserDetailResponse findByIdTest(Long id);
}
