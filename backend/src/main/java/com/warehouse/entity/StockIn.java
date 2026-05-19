package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("stock_in")
public class StockIn {
    private Integer id;
    private Integer productId;
    private Integer quantity;
    private Integer operatorId;
    private String status;  // pending / confirmed
    private Date operateTime;
    private String remark;
}