package com.example.springboot_basic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private int productId;
    private String name;
    private int quantity;
    private double unitPrice;
}
