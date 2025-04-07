package com.shechubbb.smdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shechubbb.smdc.common.exception.BusinessException;
import com.shechubbb.smdc.entity.Category;
import com.shechubbb.smdc.entity.Dish;
import com.shechubbb.smdc.mapper.CategoryMapper;
import com.shechubbb.smdc.mapper.DishMapper;
import com.shechubbb.smdc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishMapper dishMapper;

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 分页数据
     */
    @Override
    public Page<Category> page(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        return page(pageInfo, queryWrapper);
    }

    /**
     * 添加分类
     * @param category 分类信息
     */
    @Override
    public void add(Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        save(category);
    }

    /**
     * 更新分类
     * @param category 分类信息
     */
    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        updateById(category);
    }

    /**
     * 删除分类
     * @param id 分类ID
     */
    @Override
    public void delete(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        if (dishMapper.exists(queryWrapper)) {
            throw new BusinessException("该分类下有菜品，不能删除");
        }
        removeById(id);
    }

    /**
     * 获取分类列表
     * @return 分类列表
     */
    @Override
    public List<Category> list() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        return list(queryWrapper);
    }
} 