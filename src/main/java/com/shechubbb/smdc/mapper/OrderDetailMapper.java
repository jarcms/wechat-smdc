package com.shechubbb.smdc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shechubbb.smdc.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单明细Mapper接口
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
} 