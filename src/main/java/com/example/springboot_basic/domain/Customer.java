package com.example.springboot_basic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String password;
    private double phone;
    private Date registeredDate;
    private short status;
}
