package com.warehouse.controller;

import com.warehouse.dto.StockDTO;
import com.warehouse.service.StockService;
import com.warehouse.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/in")
    public Result<Void> stockIn(@RequestBody StockDTO dto, HttpServletRequest request) {
        Integer operatorId = (Integer) request.getAttribute("userId");
        return stockService.stockIn(dto, operatorId);
    }

    @PostMapping("/out")
    public Result<Void> stockOut(@RequestBody StockDTO dto, HttpServletRequest request) {
        Integer operatorId = (Integer) request.getAttribute("userId");
        return stockService.stockOut(dto, operatorId);
    }

    @PutMapping("/in/confirm/{id}")
    public Result<Void> confirmStockIn(@PathVariable Integer id) {
        return stockService.confirmStockIn(id);
    }

    @PutMapping("/out/confirm/{id}")
    public Result<Void> confirmStockOut(@PathVariable Integer id) {
        return stockService.confirmStockOut(id);
    }
}
