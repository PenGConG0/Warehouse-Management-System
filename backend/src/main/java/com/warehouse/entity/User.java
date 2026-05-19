package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`user`")
public class User {
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private String role;  // admin / user
    private Date createTime;
}