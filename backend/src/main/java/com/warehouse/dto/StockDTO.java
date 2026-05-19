package com.warehouse.dto;

import lombok.Data;

@Data
public class StockDTO {
    private Integer productId;
    private Integer quantity;
    private String remark;
}
