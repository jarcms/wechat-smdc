package com.shechubbb.smdc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shechubbb.smdc.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜品分类Mapper接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
} 