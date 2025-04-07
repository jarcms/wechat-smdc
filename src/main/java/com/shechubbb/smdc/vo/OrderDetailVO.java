package com.shechubbb.smdc.vo;

import com.shechubbb.smdc.entity.OrderDetail;
import lombok.Data;

/**
 * 订单详情视图对象
 */
@Data
public class OrderDetailVO extends OrderDetail {

    /**
     * 菜品名称
     */
    private String dishName;

    /**
     * 规格名称
     */
    private String specificationName;
}