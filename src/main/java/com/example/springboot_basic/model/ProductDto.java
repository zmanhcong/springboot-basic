package com.example.springboot_basic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private int productId;
    private String name;
    private int quantity;
    private  double unitPrice;
    private String image;
    private String description;
    private double discount;
    private Date enteredDate;
    private short status;
    private int categoryId;
}
