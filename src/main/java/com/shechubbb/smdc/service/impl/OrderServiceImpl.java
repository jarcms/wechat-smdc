package com.shechubbb.smdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shechubbb.smdc.common.exception.BusinessException;
import com.shechubbb.smdc.entity.Order;
import com.shechubbb.smdc.entity.OrderDetail;
import com.shechubbb.smdc.entity.TableInfo;
import com.shechubbb.smdc.entity.User;
import com.shechubbb.smdc.mapper.OrderMapper;
import com.shechubbb.smdc.service.OrderDetailService;
import com.shechubbb.smdc.service.OrderService;
import com.shechubbb.smdc.service.TableInfoService;
import com.shechubbb.smdc.service.UserService;
import com.shechubbb.smdc.vo.OrderDetailVO;
import com.shechubbb.smdc.vo.OrderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private TableInfoService tableInfoService;

    /**
     * 创建订单
     * @param orderVO 订单信息
     * @return 订单ID
     */
    @Override
    @Transactional
    public Long createOrder(OrderVO orderVO) {
        // 查询桌位
        TableInfo tableInfo = tableInfoService.getById(orderVO.getTableId());
        if (tableInfo == null) {
            throw new BusinessException("桌位不存在");
        }
        
        // 设置订单基本信息
        Order order = new Order();
        BeanUtils.copyProperties(orderVO, order);
        
        // 生成订单号
        String number = generateOrderNumber();
        order.setNumber(number);
        
        // 设置订单状态
        order.setStatus(1); // 1待付款
        order.setPayStatus(0); // 0未支付
        
        // 设置创建时间
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 保存订单
        save(order);
        
        // 保存订单明细
        List<OrderDetail> orderDetails = orderVO.getOrderDetails().stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail);
            return orderDetail;
        }).collect(Collectors.toList());
        
        orderDetailService.saveBatch(orderDetails, order.getId());
        
        // 更新桌位状态
        tableInfo.setStatus(1); // 使用中
        tableInfo.setUpdateTime(LocalDateTime.now());
        tableInfoService.updateById(tableInfo);
        
        return order.getId();
    }

    /**
     * 支付订单
     * @param id 订单ID
     * @param payMethod 支付方式
     */
    @Override
    @Transactional
    public void payOrder(Long id, Integer payMethod) {
        // 查询订单
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 检查订单状态
        if (order.getStatus() != 1) {
            throw new BusinessException("订单状态异常，不能支付");
        }
        
        // 更新订单状态
        order.setStatus(2); // 2待接单
        order.setPayMethod(payMethod);
        order.setPayStatus(1); // 1已支付
        order.setUpdateTime(LocalDateTime.now());
        
        updateById(order);
    }

    /**
     * 取消订单
     * @param id 订单ID
     */
    @Override
    @Transactional
    public void cancelOrder(Long id) {
        // 查询订单
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 检查订单状态
        if (order.getStatus() == 4 || order.getStatus() == 5) {
            throw new BusinessException("订单已完成或已取消，不能取消");
        }
        
        // 更新订单状态
        order.setStatus(5); // 5已取消
        order.setUpdateTime(LocalDateTime.now());
        
        updateById(order);
        
        // 更新桌位状态
        TableInfo tableInfo = tableInfoService.getById(order.getTableId());
        if (tableInfo != null) {
            tableInfo.setStatus(0); // 空闲
            tableInfo.setUpdateTime(LocalDateTime.now());
            tableInfoService.updateById(tableInfo);
        }
    }

    /**
     * 接单
     * @param id 订单ID
     */
    @Override
    public void acceptOrder(Long id) {
        // 查询订单
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 检查订单状态
        if (order.getStatus() != 2) {
            throw new BusinessException("订单状态异常，不能接单");
        }
        
        // 更新订单状态
        order.setStatus(3); // 3待上菜
        order.setUpdateTime(LocalDateTime.now());
        
        updateById(order);
    }

    /**
     * 完成订单
     * @param id 订单ID
     */
    @Override
    @Transactional
    public void completeOrder(Long id) {
        // 查询订单
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 检查订单状态
        if (order.getStatus() != 3) {
            throw new BusinessException("订单状态异常，不能完成");
        }
        
        // 更新订单状态
        order.setStatus(4); // 4已完成
        order.setUpdateTime(LocalDateTime.now());
        
        updateById(order);
        
        // 更新桌位状态
        TableInfo tableInfo = tableInfoService.getById(order.getTableId());
        if (tableInfo != null) {
            tableInfo.setStatus(0); // 空闲
            tableInfo.setUpdateTime(LocalDateTime.now());
            tableInfoService.updateById(tableInfo);
        }
    }

    /**
     * 根据ID查询订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    @Override
    public OrderVO getOrderDetail(Long id) {
        // 查询订单
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 转换为OrderVO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        
        // 查询用户信息
        User user = userService.getById(order.getUserId());
        if (user != null) {
            orderVO.setUserName(user.getNickname());
        }
        
        // 查询桌位信息
        TableInfo tableInfo = tableInfoService.getById(order.getTableId());
        if (tableInfo != null) {
            orderVO.setTableName(tableInfo.getName());
        }
        
        // 查询订单明细
        List<OrderDetailVO> orderDetailVOs = orderDetailService.getByOrderId(id);
        orderVO.setOrderDetails(orderDetailVOs);
        
        return orderVO;
    }

    /**
     * 用户订单分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @param userId 用户ID
     * @return 分页数据
     */
    @Override
    public Page<OrderVO> userPage(int page, int pageSize, Long userId) {
        // 创建分页对象
        Page<Order> pageInfo = new Page<>(page, pageSize);
        
        // 构造查询条件
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        queryWrapper.orderByDesc(Order::getCreateTime);
        
        // 执行查询
        page(pageInfo, queryWrapper);
        
        // 转换为OrderVO分页对象
        return handleOrderVOPage(pageInfo);
    }

    /**
     * 管理端订单分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @param number 订单号
     * @param status 订单状态
     * @return 分页数据
     */
    @Override
    public Page<OrderVO> adminPage(int page, int pageSize, String number, Integer status) {
        // 创建分页对象
        Page<Order> pageInfo = new Page<>(page, pageSize);
        
        // 构造查询条件
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(number), Order::getNumber, number);
        queryWrapper.eq(status != null, Order::getStatus, status);
        queryWrapper.orderByDesc(Order::getCreateTime);
        
        // 执行查询
        page(pageInfo, queryWrapper);
        
        // 转换为OrderVO分页对象
        return handleOrderVOPage(pageInfo);
    }
    
    /**
     * 处理OrderVO分页对象
     * @param pageInfo 订单分页对象
     * @return OrderVO分页对象
     */
    private Page<OrderVO> handleOrderVOPage(Page<Order> pageInfo) {
        // 构造OrderVO分页对象
        Page<OrderVO> orderVOPage = new Page<>();
        BeanUtils.copyProperties(pageInfo, orderVOPage, "records");
        
        // 处理订单记录
        List<OrderVO> orderVOList = pageInfo.getRecords().stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            
            // 查询用户信息
            User user = userService.getById(order.getUserId());
            if (user != null) {
                orderVO.setUserName(user.getNickname());
            }
            
            // 查询桌位信息
            TableInfo tableInfo = tableInfoService.getById(order.getTableId());
            if (tableInfo != null) {
                orderVO.setTableName(tableInfo.getName());
            }
            
            return orderVO;
        }).collect(Collectors.toList());
        
        orderVOPage.setRecords(orderVOList);
        
        return orderVOPage;
    }
    
    /**
     * 生成订单号
     * @return 订单号
     */
    private String generateOrderNumber() {
        // 格式：时间戳 + 6位随机数
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomNum = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        return timestamp + randomNum;
    }
} 