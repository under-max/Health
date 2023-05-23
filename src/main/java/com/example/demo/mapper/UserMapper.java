package com.example.demo.mapper;

import com.example.demo.entity.User;
import com.example.demo.request.UserEdit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {


    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    Optional<User> findById(Long authedUserId);
    void save(User user);

    void update(UserEdit userEdit);

    void delete(Long id);

    List<User> findAll(int page);
}
