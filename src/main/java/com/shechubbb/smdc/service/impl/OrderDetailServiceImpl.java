package com.shechubbb.smdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shechubbb.smdc.entity.Dish;
import com.shechubbb.smdc.entity.OrderDetail;
import com.shechubbb.smdc.entity.Specification;
import com.shechubbb.smdc.mapper.DishMapper;
import com.shechubbb.smdc.mapper.OrderDetailMapper;
import com.shechubbb.smdc.service.OrderDetailService;
import com.shechubbb.smdc.service.SpecificationService;
import com.shechubbb.smdc.vo.OrderDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单明细服务实现类
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据订单ID查询订单明细
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    @Override
    public List<OrderDetailVO> getByOrderId(Long orderId) {
        // 查询订单明细
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetails = list(queryWrapper);
        
        if (orderDetails == null || orderDetails.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 转换为OrderDetailVO
        return orderDetails.stream().map(orderDetail -> {
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            BeanUtils.copyProperties(orderDetail, orderDetailVO);
            
            // 设置菜品名称，使用DishMapper替代DishService
            Dish dish = dishMapper.selectById(orderDetail.getDishId());
            if (dish != null) {
                orderDetailVO.setDishName(dish.getName());
            }
            
            // 设置规格名称
            if (orderDetail.getSpecificationId() != null) {
                Specification specification = specificationService.getById(orderDetail.getSpecificationId());
                if (specification != null) {
                    orderDetailVO.setSpecificationName(specification.getName());
                }
            }
            
            return orderDetailVO;
        }).collect(Collectors.toList());
    }

    /**
     * 批量保存订单明细
     * @param orderDetails 订单明细列表
     * @param orderId 订单ID
     */
    @Override
    public void saveBatch(List<OrderDetail> orderDetails, Long orderId) {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return;
        }
        
        // 设置订单ID和创建时间
        List<OrderDetail> details = orderDetails.stream().map(item -> {
            item.setOrderId(orderId);
            item.setCreateTime(LocalDateTime.now());
            return item;
        }).collect(Collectors.toList());
        
        // 批量保存
        saveBatch(details);
    }
} 