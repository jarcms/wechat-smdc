package com.shechubbb.smdc.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shechubbb.smdc.common.result.Result;
import com.shechubbb.smdc.entity.TableInfo;
import com.shechubbb.smdc.service.TableInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 桌位管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/table")
public class AdminTableController {

    @Autowired
    private TableInfoService tableInfoService;

    /**
     * 桌位分页查询
     */
    @GetMapping("/page")
    public Result<Page<TableInfo>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<TableInfo> pageInfo = tableInfoService.page(page, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 添加桌位
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody TableInfo tableInfo) {
        tableInfoService.add(tableInfo);
        return Result.success();
    }

    /**
     * 更新桌位
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody TableInfo tableInfo) {
        tableInfoService.update(tableInfo);
        return Result.success();
    }

    /**
     * 删除桌位
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        tableInfoService.delete(id);
        return Result.success();
    }

    /**
     * 生成桌位二维码
     */
    @GetMapping("/qrcode/{id}")
    public Result<String> qrcode(@PathVariable Long id) {
        String qrCode = tableInfoService.generateQrCode(id);
        return Result.success(qrCode);
    }
} 