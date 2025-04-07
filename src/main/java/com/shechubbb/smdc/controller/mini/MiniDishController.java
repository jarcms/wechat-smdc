package com.shechubbb.smdc.controller.mini;

import com.shechubbb.smdc.common.result.Result;
import com.shechubbb.smdc.service.DishService;
import com.shechubbb.smdc.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序菜品控制器
 */
@Slf4j
@RestController
@RequestMapping("/mini/dish")
public class MiniDishController {

    @Autowired
    private DishService dishService;

    /**
     * 根据分类ID获取菜品列表
     */
    @GetMapping("/list/{categoryId}")
    public Result<List<DishVO>> list(@PathVariable Long categoryId) {
        List<DishVO> list = dishService.listByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 获取菜品详情
     */
    @GetMapping("/detail/{id}")
    public Result<DishVO> detail(@PathVariable Long id) {
        DishVO dishVO = dishService.getWithSpecification(id);
        return Result.success(dishVO);
    }
} 