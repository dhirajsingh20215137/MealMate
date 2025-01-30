package com.malemate.demo.Dao;

import com.malemate.demo.entity.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(int id);


    User saveUser(User user);


    void deleteUser(int id);


    Optional<User> getUserByEmail(String email);
}
