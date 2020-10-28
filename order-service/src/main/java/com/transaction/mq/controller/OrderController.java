package com.transaction.mq.controller;

import com.transaction.mq.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @category 订单生成控制器
 * @author Ellen
 * @date 2020
 * @description 用于生成订单，测试
 */
@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService service;

    /**
     * 用于生成订单 这里只是测试 生成订单就减库存
     * 实际上，减库存应该在支付成功的回调中实现
     *
     * @return
     */
    @GetMapping("test")
    @ResponseBody
    public String makeOrder() {
        if (service.makeOrder()) {
            return "订单生成成功！";
        }
        return "订单生成失败！";
    }
}
