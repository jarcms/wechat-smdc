package com.shechubbb.smdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shechubbb.smdc.common.exception.BusinessException;
import com.shechubbb.smdc.entity.Category;
import com.shechubbb.smdc.entity.Dish;
import com.shechubbb.smdc.entity.Specification;
import com.shechubbb.smdc.mapper.CategoryMapper;
import com.shechubbb.smdc.mapper.DishMapper;
import com.shechubbb.smdc.service.DishService;
import com.shechubbb.smdc.service.SpecificationService;
import com.shechubbb.smdc.vo.DishVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品服务实现类
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    // 将CategoryService替换为CategoryMapper
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SpecificationService specificationService;

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @param name 菜品名称
     * @return 分页数据
     */
    @Override
    public Page<DishVO> page(int page, int pageSize, String name) {
        // 创建分页对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        
        // 构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        
        // 执行查询
        page(pageInfo, queryWrapper);
        
        // 构造DishVO分页对象
        Page<DishVO> dishVOPage = new Page<>();
        BeanUtils.copyProperties(pageInfo, dishVOPage, "records");
        
        // 处理分类名称
        List<DishVO> dishVOList = pageInfo.getRecords().stream().map(dish -> {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);
            
            // 设置分类名称，使用CategoryMapper替代CategoryService
            Category category = categoryMapper.selectById(dish.getCategoryId());
            if (category != null) {
                dishVO.setCategoryName(category.getName());
            }
            
            return dishVO;
        }).collect(Collectors.toList());
        
        dishVOPage.setRecords(dishVOList);
        
        return dishVOPage;
    }

    /**
     * 添加菜品
     * @param dishVO 菜品信息
     */
    @Override
    @Transactional
    public void add(DishVO dishVO) {
        // 保存菜品基本信息
        dishVO.setCreateTime(LocalDateTime.now());
        dishVO.setUpdateTime(LocalDateTime.now());
        save(dishVO);
        
        // 保存规格信息
        List<Specification> specifications = dishVO.getSpecifications();
        if (specifications != null && !specifications.isEmpty()) {
            specificationService.saveBatch(specifications, dishVO.getId());
        }
    }

    /**
     * 更新菜品
     * @param dishVO 菜品信息
     */
    @Override
    @Transactional
    public void update(DishVO dishVO) {
        // 更新菜品基本信息
        dishVO.setUpdateTime(LocalDateTime.now());
        updateById(dishVO);
        
        // 更新规格信息
        List<Specification> specifications = dishVO.getSpecifications();
        if (specifications != null) {
            specificationService.saveBatch(specifications, dishVO.getId());
        }
    }

    /**
     * 删除菜品
     * @param id 菜品ID
     */
    @Override
    @Transactional
    public void delete(Long id) {
        // 删除菜品
        removeById(id);
        
        // 删除规格
        specificationService.deleteByDishId(id);
    }

    /**
     * 更新菜品状态
     * @param id 菜品ID
     * @param status 状态
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        Dish dish = getById(id);
        if (dish == null) {
            throw new BusinessException("菜品不存在");
        }
        
        dish.setStatus(status);
        dish.setUpdateTime(LocalDateTime.now());
        updateById(dish);
    }

    /**
     * 根据分类ID查询菜品列表
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    @Override
    public List<DishVO> listByCategoryId(Long categoryId) {
        // 查询菜品
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        queryWrapper.eq(Dish::getStatus, 1); // 只查询在售的菜品
        queryWrapper.orderByAsc(Dish::getSort);
        
        List<Dish> dishList = list(queryWrapper);
        
        // 转换为DishVO并查询规格
        return dishList.stream().map(dish -> {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);
            
            // 查询规格信息
            List<Specification> specifications = specificationService.listByDishId(dish.getId());
            dishVO.setSpecifications(specifications);
            
            return dishVO;
        }).collect(Collectors.toList());
    }

    /**
     * 根据ID查询菜品和规格信息
     * @param id 菜品ID
     * @return 菜品信息
     */
    @Override
    public DishVO getWithSpecification(Long id) {
        // 查询菜品基本信息
        Dish dish = getById(id);
        if (dish == null) {
            throw new BusinessException("菜品不存在");
        }
        
        // 转换为DishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        
        // 查询规格信息
        List<Specification> specifications = specificationService.listByDishId(id);
        dishVO.setSpecifications(specifications);
        
        // 设置分类名称，使用CategoryMapper替代CategoryService
        Category category = categoryMapper.selectById(dish.getCategoryId());
        if (category != null) {
            dishVO.setCategoryName(category.getName());
        }
        
        return dishVO;
    }
}