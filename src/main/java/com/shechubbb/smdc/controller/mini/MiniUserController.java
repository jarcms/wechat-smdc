package com.shechubbb.smdc.controller.mini;

import com.shechubbb.smdc.common.result.Result;
import com.shechubbb.smdc.entity.User;
import com.shechubbb.smdc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 小程序用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/mini/user")
public class MiniUserController {

    @Autowired
    private UserService userService;

    /**
     * 微信登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> loginMap) {
        String code = loginMap.get("code");
        User user = userService.wxLogin(code);
        return Result.success(user);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info/{id}")
    public Result<User> info(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success();
    }
}