package com.shechubbb.smdc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shechubbb.smdc.entity.Dish;
import com.shechubbb.smdc.vo.DishVO;

import java.util.List;

/**
 * 菜品服务接口
 */
public interface DishService extends IService<Dish> {

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @param name 菜品名称
     * @return 分页数据
     */
    Page<DishVO> page(int page, int pageSize, String name);

    /**
     * 添加菜品
     * @param dishVO 菜品信息
     */
    void add(DishVO dishVO);

    /**
     * 更新菜品
     * @param dishVO 菜品信息
     */
    void update(DishVO dishVO);

    /**
     * 删除菜品
     * @param id 菜品ID
     */
    void delete(Long id);

    /**
     * 更新菜品状态
     * @param id 菜品ID
     * @param status 状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据分类ID查询菜品列表
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    List<DishVO> listByCategoryId(Long categoryId);

    /**
     * 根据ID查询菜品和规格信息
     * @param id 菜品ID
     * @return 菜品信息
     */
    DishVO getWithSpecification(Long id);
} 