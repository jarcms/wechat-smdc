package com.shechubbb.smdc.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shechubbb.smdc.common.result.Result;
import com.shechubbb.smdc.service.DishService;
import com.shechubbb.smdc.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 菜品管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class AdminDishController {

    @Autowired
    private DishService dishService;

    /**
     * 菜品分页查询
     */
    @GetMapping("/page")
    public Result<Page<DishVO>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<DishVO> pageInfo = dishService.page(page, pageSize, name);
        return Result.success(pageInfo);
    }

    /**
     * 根据ID获取菜品信息
     */
    @GetMapping("/info/{id}")
    public Result<DishVO> info(@PathVariable Long id) {
        DishVO dishVO = dishService.getWithSpecification(id);
        return Result.success(dishVO);
    }

    /**
     * 添加菜品
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody DishVO dishVO) {
        dishService.add(dishVO);
        return Result.success();
    }

    /**
     * 更新菜品
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody DishVO dishVO) {
        dishService.update(dishVO);
        return Result.success();
    }

    /**
     * 删除菜品
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        dishService.delete(id);
        return Result.success();
    }

    /**
     * 更新菜品状态
     */
    @PostMapping("/status")
    public Result<Void> status(@RequestBody Map<String, Object> map) {
        Long id = Long.valueOf(map.get("id").toString());
        Integer status = Integer.valueOf(map.get("status").toString());
        
        dishService.updateStatus(id, status);
        return Result.success();
    }
} 