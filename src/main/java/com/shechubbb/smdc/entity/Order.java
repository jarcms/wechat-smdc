package com.shechubbb.smdc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName("`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String number;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 桌位ID
     */
    private Long tableId;

    /**
     * 总金额
     */
    private BigDecimal amount;

    /**
     * 状态：1待付款，2待接单，3待上菜，4已完成，5已取消
     */
    private Integer status;

    /**
     * 支付方式：1微信支付，2支付宝支付
     */
    private Integer payMethod;

    /**
     * 支付状态：0未支付，1已支付
     */
    private Integer payStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}