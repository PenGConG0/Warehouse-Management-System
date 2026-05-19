package com.warehouse.service;

import com.warehouse.entity.Product;
import com.warehouse.mapper.ProductMapper;
import com.warehouse.util.Result;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Result<List<Product>> getProductList() {
        return Result.success(productMapper.selectList(null));
    }

    public Result<Void> addProduct(Product product) {
        product.setCreateTime(new Date());
        productMapper.insert(product);
        return Result.success();
    }

    public Result<Void> deleteProduct(Integer id) {
        productMapper.deleteById(id);
        return Result.success();
    }
}
