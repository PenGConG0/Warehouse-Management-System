package com.warehouse.service;

import com.warehouse.dto.StockDTO;
import com.warehouse.entity.Product;
import com.warehouse.entity.StockIn;
import com.warehouse.entity.StockOut;
import com.warehouse.mapper.ProductMapper;
import com.warehouse.mapper.StockInMapper;
import com.warehouse.mapper.StockOutMapper;
import com.warehouse.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class StockService {

    private final StockInMapper stockInMapper;
    private final StockOutMapper stockOutMapper;
    private final ProductMapper productMapper;

    public StockService(StockInMapper stockInMapper, StockOutMapper stockOutMapper, ProductMapper productMapper) {
        this.stockInMapper = stockInMapper;
        this.stockOutMapper = stockOutMapper;
        this.productMapper = productMapper;
    }

    // ========== 入库 ==========

    public Result<Void> stockIn(StockDTO dto, Integer operatorId) {
        StockIn record = new StockIn();
        record.setProductId(dto.getProductId());
        record.setQuantity(dto.getQuantity());
        record.setOperatorId(operatorId);
        record.setStatus("pending");
        record.setOperateTime(new Date());
        record.setRemark(dto.getRemark());
        stockInMapper.insert(record);
        return Result.success();
    }

    @Transactional
    public Result<Void> confirmStockIn(Integer id) {
        StockIn record = stockInMapper.selectById(id);
        if (record == null) {
            return Result.error("记录不存在");
        }
        if (!"pending".equals(record.getStatus())) {
            return Result.error("该记录已确认");
        }

        record.setStatus("confirmed");
        stockInMapper.updateById(record);

        Product product = productMapper.selectById(record.getProductId());
        if (product != null) {
            product.setStockQuantity(product.getStockQuantity() + record.getQuantity());
            productMapper.updateById(product);
        }

        return Result.success();
    }

    public Result<List<StockIn>> getStockInRecords() {
        return Result.success(stockInMapper.selectList(null));
    }

    // ========== 出库 ==========

    public Result<Void> stockOut(StockDTO dto, Integer operatorId) {
        StockOut record = new StockOut();
        record.setProductId(dto.getProductId());
        record.setQuantity(dto.getQuantity());
        record.setOperatorId(operatorId);
        record.setStatus("pending");
        record.setOperateTime(new Date());
        record.setRemark(dto.getRemark());
        stockOutMapper.insert(record);
        return Result.success();
    }

    @Transactional
    public Result<Void> confirmStockOut(Integer id) {
        StockOut record = stockOutMapper.selectById(id);
        if (record == null) {
            return Result.error("记录不存在");
        }
        if (!"pending".equals(record.getStatus())) {
            return Result.error("该记录已确认");
        }

        Product product = productMapper.selectById(record.getProductId());
        if (product == null) {
            return Result.error("产品不存在");
        }
        if (product.getStockQuantity() < record.getQuantity()) {
            return Result.error("库存不足");
        }

        record.setStatus("confirmed");
        stockOutMapper.updateById(record);

        product.setStockQuantity(product.getStockQuantity() - record.getQuantity());
        productMapper.updateById(product);

        return Result.success();
    }

    public Result<List<StockOut>> getStockOutRecords() {
        return Result.success(stockOutMapper.selectList(null));
    }
}
