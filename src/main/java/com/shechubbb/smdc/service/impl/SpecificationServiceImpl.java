package com.shechubbb.smdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shechubbb.smdc.entity.Specification;
import com.shechubbb.smdc.mapper.SpecificationMapper;
import com.shechubbb.smdc.service.SpecificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规格服务实现类
 */
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements SpecificationService {

    /**
     * 根据菜品ID查询规格列表
     * @param dishId 菜品ID
     * @return 规格列表
     */
    @Override
    public List<Specification> listByDishId(Long dishId) {
        LambdaQueryWrapper<Specification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Specification::getDishId, dishId);
        return list(queryWrapper);
    }

    /**
     * 添加规格
     * @param specification 规格信息
     */
    @Override
    public void add(Specification specification) {
        specification.setCreateTime(LocalDateTime.now());
        specification.setUpdateTime(LocalDateTime.now());
        save(specification);
    }

    /**
     * 更新规格
     * @param specification 规格信息
     */
    @Override
    public void update(Specification specification) {
        specification.setUpdateTime(LocalDateTime.now());
        updateById(specification);
    }

    /**
     * 删除规格
     * @param id 规格ID
     */
    @Override
    public void delete(Long id) {
        removeById(id);
    }

    /**
     * 批量保存规格
     * @param specifications 规格列表
     * @param dishId 菜品ID
     */
    @Override
    @Transactional
    public void saveBatch(List<Specification> specifications, Long dishId) {
        // 先删除原有规格
        deleteByDishId(dishId);
        
        // 设置菜品ID和创建时间
        List<Specification> specList = specifications.stream().map(item -> {
            item.setDishId(dishId);
            item.setCreateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            return item;
        }).collect(Collectors.toList());
        
        // 批量保存
        saveBatch(specList);
    }

    /**
     * 根据菜品ID删除规格
     * @param dishId 菜品ID
     */
    @Override
    public void deleteByDishId(Long dishId) {
        LambdaQueryWrapper<Specification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Specification::getDishId, dishId);
        remove(queryWrapper);
    }
} 