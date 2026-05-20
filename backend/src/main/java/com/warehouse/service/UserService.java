package com.warehouse.service;

import com.warehouse.dto.LoginDTO;
import com.warehouse.entity.User;
import com.warehouse.mapper.UserMapper;
import com.warehouse.util.JwtUtil;
import com.warehouse.util.MD5Util;
import com.warehouse.util.Result;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    public Result<Map<String, Object>> login(LoginDTO dto) {
        User user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, dto.getUsername())
        );
        if (user == null || !user.getPassword().equals(MD5Util.md5(dto.getPassword()))) {
            return Result.error("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("realName", user.getRealName());
        data.put("role", user.getRole());
        data.put("token", token);
        return Result.success(data);
    }

    public Result<List<User>> getUserList() {
        List<User> users = userMapper.selectList(null);
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    public Result<Void> addUser(User user) {
        user.setPassword(MD5Util.md5("123456"));
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return Result.success();
    }

    public Result<Void> deleteUser(Integer id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    public Result<Void> changePassword(Integer id, String newPassword) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(MD5Util.md5(newPassword));
        userMapper.updateById(user);
        return Result.success();
    }
}
