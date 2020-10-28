package com.transaction.mq.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.transaction.mq.config.RabbitMqConfig;
import com.transaction.mq.domain.OrderCutTask;
import com.transaction.mq.service.CutStockService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @category 商品减库存任务
 * @author Ellen
 * @date 2020
 * @description
 */
@Component
public class StockTask {
    @Autowired
    private CutStockService cutStockService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 减库存消费者
     * @param jsonString
     */
    @RabbitListener(queues = RabbitMqConfig.ORDER_GOODS_CUT_STOCK_QUEUE)
    public void cutStockConsumer(String jsonString) {
        OrderCutTask orderCutTask = JSON.parseObject(jsonString, OrderCutTask.class);

        Map map = JSON.parseObject(orderCutTask.getMsg_content(), Map.class);
        Integer goodsId = (Integer) map.get("goodsId");
        Integer buyNumber = (Integer) map.get("buyNumber");
        // 减库存，增记录
        Boolean result = cutStockService.cutGoodsStock(goodsId, buyNumber, orderCutTask);

        if (result) {
            System.out.println("任务[" + orderCutTask.getId() + "]完成减库存！");
            // 发送成功得消息给order
            rabbitTemplate.convertAndSend(
                    RabbitMqConfig.ORDER_GOODS_EXCHANGE, RabbitMqConfig.ORDER_GOODS_CUT_STOCK_FINISH_KEY, jsonString);
        }

    }
}
