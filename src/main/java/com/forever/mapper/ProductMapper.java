package com.forever.mapper;

import com.forever.model.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper {
    @Select("select * from tb_product where product_id=#{id}")
    Product getProductById(Long id);

    @Update("update tb_product set product_name=#{productName},product_desc=#{productDesc} WHERE product_id=#{productId}")
    int updateProduct(Product product);

    @Delete("delete from tb_product where product_id=#{id}")
    void deleteProductById(Long id);

    @Select("select * from tb_product where product_name=#{productName}")
    Product getProductByName(String productName);
}
