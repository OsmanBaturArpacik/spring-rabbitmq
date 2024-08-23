package com.microservice.ms1.service.service;

import com.microservice.ms1.service.api.config.RabbitMqConfig;
import com.microservice.ms1.service.api.model.ProductDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final JdbcTemplate jdbcTemplate;

    private final RabbitTemplate rabbitTemplate;


    public ProductService(JdbcTemplate jdbcTemplate, RabbitTemplate rabbitTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void updateProductPriceById(String id, double price) {
        if (id == null) {
            return;
        }
        String sqlQuery = "SELECT price " +
                        "FROM product_tbl " +
                        "WHERE id = ?";
        String oldPrice = jdbcTemplate.queryForObject(sqlQuery, new Object[]{id}, String.class);
        if (oldPrice == null) {
            oldPrice = "old price isn't exist";
        }
        String query =
                "UPDATE product_tbl " +
                "SET price = ? " +
                "WHERE productId = ?";
        int rowsAffected  = jdbcTemplate.update(query, price, id);

        if (rowsAffected > 0) {
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY + "." + RabbitMqConfig.MY_QUEUE, "id: " + id + " old price: " + oldPrice +  " new price: " + price);
            return;
        } else {
            return;
        }
    }

    public List<ProductDto> getAll() {
        String query = "SELECT productId, price FROM product_tbl";
        return jdbcTemplate.query(query, new ProductRowMapper());
    }
}
