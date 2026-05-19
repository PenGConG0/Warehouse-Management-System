package com.warehouse.controller;

import com.warehouse.entity.Product;
import com.warehouse.service.ProductService;
import com.warehouse.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public Result<List<Product>> list() {
        return productService.getProductList();
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }
}
