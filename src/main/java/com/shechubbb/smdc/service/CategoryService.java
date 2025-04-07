package com.shechubbb.smdc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shechubbb.smdc.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 分页数据
     */
    Page<Category> page(int page, int pageSize);

    /**
     * 添加分类
     * @param category 分类信息
     */
    void add(Category category);

    /**
     * 更新分类
     * @param category 分类信息
     */
    void update(Category category);

    /**
     * 删除分类
     * @param id 分类ID
     */
    void delete(Long id);

    /**
     * 获取分类列表
     * @return 分类列表
     */
    List<Category> list();
} 