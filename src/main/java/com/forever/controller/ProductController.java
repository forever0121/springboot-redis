package com.forever.controller;

import com.forever.model.Product;
import com.forever.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        Product product = productService.getProducrById(id);
        System.out.println(product);
        return product;
    }

    @GetMapping("/product/deleteCache")
    public String deleteCache() {
        productService.deleteCache();
        return "success";
    }
}
