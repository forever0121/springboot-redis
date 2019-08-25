package com.forever.service;

import com.forever.mapper.ProductMapper;
import com.forever.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Cacheable(cacheNames = "product",key = "#root.methodName+'['+#id+']'")
    public Product getProducrById(Long id){
        return productMapper.getProductById(id);
    }
}
