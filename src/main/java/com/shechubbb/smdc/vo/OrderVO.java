package com.shechubbb.smdc.vo;

import com.shechubbb.smdc.entity.Order;
import lombok.Data;

import java.util.List;

/**
 * 订单视图对象
 */
@Data
public class OrderVO extends Order {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 桌位名称
     */
    private String tableName;

    /**
     * 订单详情列表
     */
    private List<OrderDetailVO> orderDetails;
}