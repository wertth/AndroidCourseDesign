package com.example.backend.Service;


import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 李东雷
 * @TIME：17:13-2019/6/11
 */
@Service
public class UserService implements UserMapper {

    @Autowired
    UserMapper userMapper;

    @Override
    public void registerUser(User user) {
        userMapper.registerUser(user);
    }

    @Override
    public String findPasswordByUsername(String username) {
        return userMapper.findPasswordByUsername(username);
    }

    @Override
    public String getHeadpic(String username) {
        return userMapper.getHeadpic(username);
    }

    @Override
    public String findRoleByUsername(String username) {
        return userMapper.findRoleByUsername(username);
    }

    @Override
    public User selectUser(String username) {
        return userMapper.selectUser(username);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public Integer isHaveEmail(String email) {
        return userMapper.isHaveEmail(email);
    }

    @Override
    public void updateRole(String username) {
        userMapper.updateRole(username);
    }
}
