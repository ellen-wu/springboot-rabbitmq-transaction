package com.transaction.mq.service;

import com.alibaba.fastjson.JSON;
import com.transaction.mq.config.RabbitMqConfig;
import com.transaction.mq.domain.Order;
import com.transaction.mq.domain.OrderCutTask;
import com.transaction.mq.domain.OrderMsgContent;
import com.transaction.mq.mapper.OrderCutTaskMapper;
import com.transaction.mq.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderCutTaskMapper cutTaskMapper;

    @Transactional
    public Boolean makeOrder() {
        Integer timeNow = Math.toIntExact(System.currentTimeMillis() / 1000);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Random random = new Random();

        Integer goodsId = random.nextInt(3) + 1;
        Integer buyNumber = random.nextInt(9) + 1;

        // 新增订单
        Order order = new Order();
        order.setOrder_no(dateFormat.format(date) + random.nextInt(10000));
        order.setCreate_time(timeNow);
        order.setGoods_id(goodsId);
        order.setBuy_number(buyNumber);
        order.setOut_order_no(dateFormat.format(date) + random.nextInt(10000));
        order.setStatus((byte) 1);

        orderMapper.insertOne(order);

        // 订单商品id 与 购买数量
        OrderMsgContent msgContent = new OrderMsgContent();
        msgContent.setGoodsId(goodsId);
        msgContent.setBuyNumber(buyNumber);

        // 新增减库存任务记录
        OrderCutTask task = new OrderCutTask();

        Integer orderId = order.getId();
        task.setOrder_id(orderId);
        task.setTask_type((byte) 1);
        task.setStatus((byte) 1);
        task.setVersion((byte) 0);
        task.setMq_exchange(RabbitMqConfig.ORDER_GOODS_EXCHANGE);
        task.setMq_routing_key(RabbitMqConfig.ORDER_GOODS_CUT_STOCK_KEY);
        task.setMsg_content(JSON.toJSONString(msgContent));
        task.setError_msg("");
        task.setCreate_time(timeNow);
        task.setUpdate_time(timeNow);

        cutTaskMapper.insertOne(task);
        if (order.getId() > 0 && task.getId() > 0) {
            return true;
        }
        return false;
    }
}
