package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("product")
public class Product {
    private Integer id;
    private String productCode;
    private String productName;
    private String unit;
    private Integer stockQuantity;
    private String location;
    private Date createTime;
}