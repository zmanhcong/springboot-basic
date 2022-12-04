package com.example.springboot_basic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Column(columnDefinition = "nvarchar(100) not null")
    private String name;
    @Column(nullable = false)
    private int quantity;
    @Column()
    private  double unitPrice;
    @Column()
    private String image;
    @Column()
    private String description;
    @Column
    private double discount;
    @Temporal(TemporalType.DATE)
    private Date enteredDate;
    @Column()
    private short status;
//    @Column(nullable = false)
//    private int categoryId;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
