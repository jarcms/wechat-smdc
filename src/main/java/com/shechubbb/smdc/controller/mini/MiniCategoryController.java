package com.shechubbb.smdc.controller.mini;

import com.shechubbb.smdc.common.result.Result;
import com.shechubbb.smdc.entity.Category;
import com.shechubbb.smdc.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序分类控制器
 */
@Slf4j
@RestController
@RequestMapping("/mini/category")
public class MiniCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> list() {
        List<Category> list = categoryService.list();
        return Result.success(list);
    }
} 