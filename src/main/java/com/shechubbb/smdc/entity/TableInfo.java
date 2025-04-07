package com.shechubbb.smdc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 桌位实体类
 */
@Data
@TableName("table_info")
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 桌位名称
     */
    private String name;

    /**
     * 桌位二维码
     */
    private String code;

    /**
     * 状态，0空闲，1使用中
     */
    private Integer status;

    /**
     * 容纳人数
     */
    private Integer capacity;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 