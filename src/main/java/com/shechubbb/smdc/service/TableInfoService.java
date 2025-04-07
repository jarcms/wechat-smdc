package com.shechubbb.smdc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shechubbb.smdc.entity.TableInfo;

/**
 * 桌位服务接口
 */
public interface TableInfoService extends IService<TableInfo> {

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 分页数据
     */
    Page<TableInfo> page(int page, int pageSize);

    /**
     * 添加桌位
     * @param tableInfo 桌位信息
     */
    void add(TableInfo tableInfo);

    /**
     * 更新桌位
     * @param tableInfo 桌位信息
     */
    void update(TableInfo tableInfo);

    /**
     * 删除桌位
     * @param id 桌位ID
     */
    void delete(Long id);

    /**
     * 生成桌位二维码
     * @param id 桌位ID
     * @return 二维码Base64字符串
     */
    String generateQrCode(Long id);

    /**
     * 根据桌位码查询桌位
     * @param code 桌位码
     * @return 桌位信息
     */
    TableInfo getByCode(String code);
} 