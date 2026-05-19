package com.warehouse.controller;

import com.warehouse.entity.User;
import com.warehouse.service.UserService;
import com.warehouse.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public Result<List<User>> list() {
        return userService.getUserList();
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@RequestParam Integer id, @RequestParam String newPassword) {
        return userService.changePassword(id, newPassword);
    }
}
