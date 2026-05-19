package com.warehouse.controller;

import com.warehouse.entity.StockIn;
import com.warehouse.entity.StockOut;
import com.warehouse.service.StockService;
import com.warehouse.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {

    private final StockService stockService;

    public RecordController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/in")
    public Result<List<StockIn>> stockInRecords() {
        return stockService.getStockInRecords();
    }

    @GetMapping("/out")
    public Result<List<StockOut>> stockOutRecords() {
        return stockService.getStockOutRecords();
    }
}
