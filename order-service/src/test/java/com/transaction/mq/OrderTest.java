package com.transaction.mq;

import com.transaction.mq.domain.Order;
import com.transaction.mq.domain.OrderCutTask;
import com.transaction.mq.mapper.OrderCutTaskMapper;
import com.transaction.mq.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderMapper mapper;

    @Autowired
    private OrderCutTaskMapper taskMapper;

    @Test
    public void testInsertOne() {
        Integer timeNow = Math.toIntExact(System.currentTimeMillis() / 1000);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Random random = new Random();

        Integer goodsId = random.nextInt(3) + 1;
        Integer buyNumber = random.nextInt(9) + 1;

        Order order = new Order();
        order.setOrder_no(dateFormat.format(date) + random.nextInt(10000));
        order.setCreate_time(timeNow);
        order.setGoods_id(goodsId);
        order.setBuy_number(buyNumber);
        order.setOut_order_no(dateFormat.format(date) + random.nextInt(10000));
        order.setStatus((byte) 1);

        mapper.insertOne(order);
        System.out.println(order);
    }

    @Test
    public void insertCutOne() {
        Integer timeNow = Math.toIntExact(System.currentTimeMillis() / 1000);

        OrderCutTask task = new OrderCutTask();

        Integer orderId = 120;
        task.setOrder_id(orderId);
        task.setTask_type((byte) 1);
        task.setStatus((byte) 1);
        task.setVersion((byte) 0);
        task.setMq_exchange("");
        task.setMq_routing_key("");
        task.setMsg_content("");
        task.setError_msg("");
        task.setCreate_time(timeNow);
        task.setUpdate_time(timeNow);

        taskMapper.insertOne(task);
        System.out.println(task);
    }

}
