package com.shechubbb.smdc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shechubbb.smdc.entity.Specification;

import java.util.List;

/**
 * 规格服务接口
 */
public interface SpecificationService extends IService<Specification> {

    /**
     * 根据菜品ID查询规格列表
     * @param dishId 菜品ID
     * @return 规格列表
     */
    List<Specification> listByDishId(Long dishId);

    /**
     * 添加规格
     * @param specification 规格信息
     */
    void add(Specification specification);

    /**
     * 更新规格
     * @param specification 规格信息
     */
    void update(Specification specification);

    /**
     * 删除规格
     * @param id 规格ID
     */
    void delete(Long id);

    /**
     * 批量保存规格
     * @param specifications 规格列表
     * @param dishId 菜品ID
     */
    void saveBatch(List<Specification> specifications, Long dishId);

    /**
     * 根据菜品ID删除规格
     * @param dishId 菜品ID
     */
    void deleteByDishId(Long dishId);
} 