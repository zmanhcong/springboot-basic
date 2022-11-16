package com.example.springboot_basic.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable{  /*add Serializable for check input of user*/
    private Long categoryId;
    @NotEmpty
    @Length(min = 5)
    private String name;
    private Boolean IsEdit = false;
}
