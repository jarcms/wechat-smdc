package com.shechubbb.smdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shechubbb.smdc.common.exception.BusinessException;
import com.shechubbb.smdc.entity.Employee;
import com.shechubbb.smdc.mapper.EmployeeMapper;
import com.shechubbb.smdc.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

/**
 * 员工服务实现类
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    /**
     * 员工登录
     * @param username 用户名
     * @param password 密码
     * @return 员工信息
     */
    @Override
    public Employee login(String username, String password) {
        // 1. 根据用户名查询员工
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee employee = getOne(queryWrapper);
        
        // 2. 如果没有查询到则返回登录失败
        if (employee == null) {
            throw new BusinessException("用户名不存在");
        }
        
        // 3. 密码比对，如果不一致则返回登录失败
        // 将页面提交的密码password进行md5加密处理
        String encryptPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!employee.getPassword().equals(encryptPassword)) {
            throw new BusinessException("密码错误");
        }
        
        return employee;
    }

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @param name 员工姓名
     * @return 分页数据
     */
    @Override
    public Page<Employee> page(int page, int pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        return page(pageInfo, queryWrapper);
    }

    /**
     * 添加员工
     * @param employee 员工信息
     */
    @Override
    public void add(Employee employee) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        long count = count(queryWrapper);
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 设置默认密码为123456并进行md5加密
        String defaultPassword = "123456";
        employee.setPassword(DigestUtils.md5DigestAsHex(defaultPassword.getBytes()));
        
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        
        save(employee);
    }

    /**
     * 更新员工
     * @param employee 员工信息
     */
    @Override
    public void update(Employee employee) {
        employee.setUpdateTime(LocalDateTime.now());
        // 密码不允许在此处修改
        employee.setPassword(null);
        updateById(employee);
    }

    /**
     * 删除员工
     * @param id 员工ID
     */
    @Override
    public void delete(Long id) {
        Employee employee = getById(id);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 不允许删除管理员
        if (employee.getRole() == 1) {
            throw new BusinessException("不能删除管理员");
        }
        
        removeById(id);
    }

    /**
     * 修改密码
     * @param id 员工ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Override
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        Employee employee = getById(id);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 校验旧密码
        String encryptOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!employee.getPassword().equals(encryptOldPassword)) {
            throw new BusinessException("原密码错误");
        }
        
        // 更新密码
        employee.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        employee.setUpdateTime(LocalDateTime.now());
        updateById(employee);
    }
} 