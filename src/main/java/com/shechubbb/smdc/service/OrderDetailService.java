package com.shechubbb.smdc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shechubbb.smdc.entity.OrderDetail;
import com.shechubbb.smdc.vo.OrderDetailVO;

import java.util.List;

/**
 * 订单明细服务接口
 */
public interface OrderDetailService extends IService<OrderDetail> {

    /**
     * 根据订单ID查询订单明细
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    List<OrderDetailVO> getByOrderId(Long orderId);

    /**
     * 批量保存订单明细
     * @param orderDetails 订单明细列表
     * @param orderId 订单ID
     */
    void saveBatch(List<OrderDetail> orderDetails, Long orderId);
} 