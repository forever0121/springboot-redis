package com.forever.controller;

import com.forever.model.Product;
import com.forever.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        Product product = productService.getProducrById(id);
        System.out.println(product);
        return product;
    }
}
