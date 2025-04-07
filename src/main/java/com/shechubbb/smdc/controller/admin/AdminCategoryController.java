package com.shechubbb.smdc.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shechubbb.smdc.common.result.Result;
import com.shechubbb.smdc.entity.Category;
import com.shechubbb.smdc.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 分类管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类分页查询
     */
    @GetMapping("/page")
    public Result<Page<Category>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Category> pageInfo = categoryService.page(page, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 分类列表查询
     */
    @GetMapping("/list")
    public Result<List<Category>> list() {
        List<Category> list = categoryService.list();
        return Result.success(list);
    }

    /**
     * 添加分类
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Category category) {
        categoryService.add(category);
        return Result.success();
    }

    /**
     * 更新分类
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody Category category) {
        categoryService.update(category);
        return Result.success();
    }

    /**
     * 删除分类
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        categoryService.delete(id);
        return Result.success();
    }
} 