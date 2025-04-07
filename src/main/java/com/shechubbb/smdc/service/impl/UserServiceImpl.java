package com.shechubbb.smdc.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shechubbb.smdc.common.exception.BusinessException;
import com.shechubbb.smdc.entity.User;
import com.shechubbb.smdc.mapper.UserMapper;
import com.shechubbb.smdc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private WxMaService wxMaService;

    /**
     * 微信登录
     * @param code 登录凭证
     * @return 用户信息
     */
    @Override
    public User wxLogin(String code) {
        try {
            // 获取微信用户的openId
            WxMaJscode2SessionResult sessionResult = wxMaService.getUserService().getSessionInfo(code);
            String openId = sessionResult.getOpenid();
            
            // 根据openId查询用户
            User user = getByOpenId(openId);
            
            // 如果用户不存在，则注册新用户
            if (user == null) {
                user = new User();
                user.setOpenId(openId);
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
                save(user);
            }
            
            return user;
        } catch (Exception e) {
            log.error("微信登录失败：", e);
            throw new BusinessException("微信登录失败");
        }
    }

    /**
     * 根据openId查询用户
     * @param openId 微信openId
     * @return 用户信息
     */
    @Override
    public User getByOpenId(String openId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenId, openId);
        return getOne(queryWrapper);
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     */
    @Override
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
    }
} 