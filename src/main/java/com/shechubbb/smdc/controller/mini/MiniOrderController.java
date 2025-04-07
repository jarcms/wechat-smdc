package com.shechubbb.smdc.controller.mini;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shechubbb.smdc.common.result.Result;
import com.shechubbb.smdc.entity.TableInfo;
import com.shechubbb.smdc.service.OrderService;
import com.shechubbb.smdc.service.TableInfoService;
import com.shechubbb.smdc.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 小程序订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/mini/order")
public class MiniOrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private TableInfoService tableInfoService;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody OrderVO orderVO) {
        Long orderId = orderService.createOrder(orderVO);
        return Result.success(orderId);
    }

    /**
     * 支付订单
     */
    @PostMapping("/pay")
    public Result<Void> pay(@RequestBody Map<String, Object> map) {
        Long id = Long.valueOf(map.get("id").toString());
        Integer payMethod = Integer.valueOf(map.get("payMethod").toString());
        
        orderService.payOrder(id, payMethod);
        return Result.success();
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    public Result<Void> cancel(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        orderService.cancelOrder(id);
        return Result.success();
    }

    /**
     * 订单列表
     */
    @GetMapping("/list")
    public Result<Page<OrderVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Long userId) {
        Page<OrderVO> pageInfo = orderService.userPage(page, pageSize, userId);
        return Result.success(pageInfo);
    }

    /**
     * 订单详情
     */
    @GetMapping("/detail/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        OrderVO orderVO = orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }
    
    /**
     * 扫码获取桌位信息
     */
    @GetMapping("/table/{code}")
    public Result<Long> table(@PathVariable String code) {
        // 根据桌位码查询桌位
        TableInfo tableInfo = tableInfoService.getByCode(code);
        if (tableInfo == null) {
            return Result.error("无效的桌位码");
        }
        
        return Result.success(tableInfo.getId());
    }
}