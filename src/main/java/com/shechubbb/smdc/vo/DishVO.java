package com.shechubbb.smdc.vo;

import com.shechubbb.smdc.entity.Dish;
import com.shechubbb.smdc.entity.Specification;
import lombok.Data;

import java.util.List;

/**
 * 菜品视图对象
 */
@Data
public class DishVO extends Dish {

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 规格列表
     */
    private List<Specification> specifications;
} 