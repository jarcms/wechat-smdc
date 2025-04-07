package com.shechubbb.smdc.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shechubbb.smdc.common.result.Result;
import com.shechubbb.smdc.entity.Employee;
import com.shechubbb.smdc.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 员工管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class AdminEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     */
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Map<String, String> loginMap) {
        String username = loginMap.get("username");
        String password = loginMap.get("password");
        
        Employee employee = employeeService.login(username, password);
        return Result.success(employee);
    }

    /**
     * 获取员工信息
     */
    @GetMapping("/info/{id}")
    public Result<Employee> info(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        
        // 不返回密码
        employee.setPassword(null);
        return Result.success(employee);
    }

    /**
     * 员工列表分页查询
     */
    @GetMapping("/page")
    public Result<Page<Employee>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<Employee> pageInfo = employeeService.page(page, pageSize, name);
        return Result.success(pageInfo);
    }

    /**
     * 添加员工
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return Result.success();
    }

    /**
     * 更新员工
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return Result.success();
    }

    /**
     * 删除员工
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        employeeService.delete(id);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    public Result<Void> updatePassword(@RequestBody Map<String, Object> map) {
        Long id = Long.valueOf(map.get("id").toString());
        String oldPassword = map.get("oldPassword").toString();
        String newPassword = map.get("newPassword").toString();
        
        employeeService.updatePassword(id, oldPassword, newPassword);
        return Result.success();
    }
} 