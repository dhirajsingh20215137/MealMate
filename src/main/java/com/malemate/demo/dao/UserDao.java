package com.malemate.demo.dao;

import com.malemate.demo.entity.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(int id);
    User saveUser(User user);
    Optional<User> getUserByEmail(String email);
}
