package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("stock_out")
public class StockOut {
    private Integer id;
    private Integer productId;
    private Integer quantity;
    private Integer operatorId;
    private String status;  // pending / confirmed
    private Date operateTime;
    private String remark;
}