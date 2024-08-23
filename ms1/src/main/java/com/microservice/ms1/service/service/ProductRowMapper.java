package com.microservice.ms1.service.service;

import com.microservice.ms1.service.api.model.ProductDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductRowMapper implements RowMapper<ProductDto> {
    @Override
    public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductDto product = new ProductDto();
        product.setProductId(rs.getString("productId"));
        product.setPrice(rs.getDouble("price"));
        return product;
    }

}

