package com.shechubbb.smdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shechubbb.smdc.common.exception.BusinessException;
import com.shechubbb.smdc.entity.TableInfo;
import com.shechubbb.smdc.mapper.TableInfoMapper;
import com.shechubbb.smdc.service.TableInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 桌位服务实现类
 */
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfo> implements TableInfoService {

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 分页数据
     */
    @Override
    public Page<TableInfo> page(int page, int pageSize) {
        Page<TableInfo> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<TableInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(TableInfo::getId);
        return page(pageInfo, queryWrapper);
    }

    /**
     * 添加桌位
     * @param tableInfo 桌位信息
     */
    @Override
    public void add(TableInfo tableInfo) {
        // 生成唯一桌位码
        tableInfo.setCode(generateUniqueCode());
        tableInfo.setStatus(0); // 默认空闲状态
        tableInfo.setCreateTime(LocalDateTime.now());
        tableInfo.setUpdateTime(LocalDateTime.now());
        save(tableInfo);
    }

    /**
     * 更新桌位
     * @param tableInfo 桌位信息
     */
    @Override
    public void update(TableInfo tableInfo) {
        tableInfo.setUpdateTime(LocalDateTime.now());
        updateById(tableInfo);
    }

    /**
     * 删除桌位
     * @param id 桌位ID
     */
    @Override
    public void delete(Long id) {
        TableInfo tableInfo = getById(id);
        if (tableInfo == null) {
            throw new BusinessException("桌位不存在");
        }
        
        // 检查桌位是否被占用
        if (tableInfo.getStatus() == 1) {
            throw new BusinessException("桌位正在使用中，不能删除");
        }
        
        removeById(id);
    }

    /**
     * 生成桌位二维码
     * @param id 桌位ID
     * @return 二维码Base64字符串
     */
    @Override
    public String generateQrCode(Long id) {
        TableInfo tableInfo = getById(id);
        if (tableInfo == null) {
            throw new BusinessException("桌位不存在");
        }
        
        // 在实际项目中，这里应该使用二维码生成库生成二维码图片并返回Base64字符串
        // 这里简化处理，直接返回桌位码
        return tableInfo.getCode();
    }

    /**
     * 根据桌位码查询桌位
     * @param code 桌位码
     * @return 桌位信息
     */
    @Override
    public TableInfo getByCode(String code) {
        LambdaQueryWrapper<TableInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TableInfo::getCode, code);
        return getOne(queryWrapper);
    }
    
    /**
     * 生成唯一桌位码
     * @return 唯一桌位码
     */
    private String generateUniqueCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }
} 