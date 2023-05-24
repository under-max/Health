package com.example.demo.mapper;

import com.example.demo.entity.User;
import com.example.demo.request.Login;
import com.example.demo.request.UserEdit;
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
}
