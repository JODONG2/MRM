package com.a406.mrm.model.dto;

import com.a406.mrm.model.entity.CategorySub;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategorySubResponseDto {

    public CategorySubResponseDto(CategorySub categorySub) {
        this.id = categorySub.getId();
        this.name = categorySub.getName();
        this.subtype = categorySub.getSubtype();
    }

    private int id;
    private String name;
    private int subtype;


}
