package com.shechubbb.smdc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shechubbb.smdc.entity.Employee;

/**
 * 员工服务接口
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 员工登录
     * @param username 用户名
     * @param password 密码
     * @return 员工信息
     */
    Employee login(String username, String password);

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @param name 员工姓名
     * @return 分页数据
     */
    Page<Employee> page(int page, int pageSize, String name);

    /**
     * 添加员工
     * @param employee 员工信息
     */
    void add(Employee employee);

    /**
     * 更新员工
     * @param employee 员工信息
     */
    void update(Employee employee);

    /**
     * 删除员工
     * @param id 员工ID
     */
    void delete(Long id);

    /**
     * 修改密码
     * @param id 员工ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(Long id, String oldPassword, String newPassword);
} 