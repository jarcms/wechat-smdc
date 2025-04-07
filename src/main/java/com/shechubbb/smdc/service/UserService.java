package com.shechubbb.smdc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shechubbb.smdc.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 微信登录
     * @param code 登录凭证
     * @return 用户信息
     */
    User wxLogin(String code);

    /**
     * 根据openId查询用户
     * @param openId 微信openId
     * @return 用户信息
     */
    User getByOpenId(String openId);

    /**
     * 更新用户信息
     * @param user 用户信息
     */
    void updateUser(User user);
} 