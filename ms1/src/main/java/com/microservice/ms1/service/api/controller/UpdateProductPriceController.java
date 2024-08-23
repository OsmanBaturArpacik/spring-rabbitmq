package com.microservice.ms1.service.api.controller;

import com.microservice.ms1.service.api.model.ProductDto;
import com.microservice.ms1.service.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/update")
public class UpdateProductPriceController {
    private final ProductService productService;

    public UpdateProductPriceController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/price")
    public void updateProductPriceById(@RequestParam("id") String id, @RequestParam("price") Double price) {
        if (id == null) {
            return;
        }
        productService.updateProductPriceById(id, price);
    }

    @GetMapping("/test-all")
    public List<ProductDto> getAllProductPrice() {
        System.out.println("Get all working...");
        return productService.getAll();
    }
}
