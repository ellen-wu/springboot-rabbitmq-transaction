package com.transaction.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @category RabbiqMq配置类
 * @author Ellen
 * @date 2020
 * @description
 */
@Configuration
public class RabbitMqConfig {
    // 订单减库存任务 交换机
    public static final String ORDER_GOODS_EXCHANGE = "java_order_goods_exchange";

    // 订单减库存任务 减库存路由key  由订单服务向mq发送消息
    public static final String ORDER_GOODS_CUT_STOCK_KEY = "java_order_goods_cut_stock_key";
    // 订单减库存任务 减库存队列  由订单服务向mq发送消息
    public static final String ORDER_GOODS_CUT_STOCK_QUEUE = "java_order_goods_cut_stock_queue";

    // 订单减库存任务完成 减库存完成路由key  由商品服务向mq发送消息
    public static final String ORDER_GOODS_CUT_STOCK_FINISH_KEY = "java_order_goods_cut_stock_finish_key";
    // 订单减库存任务完成 减库存完成队列  由商品服务向mq发送消息
    public static final String ORDER_GOODS_CUT_STOCK_FINISH_QUEUE = "java_order_goods_cut_stock_finish_queue";

    /**
     * 交换机配置
     *
     * @return exchange
     */
    @Bean(ORDER_GOODS_EXCHANGE)
    public Exchange exchangeDeclare() {
        return ExchangeBuilder.directExchange(ORDER_GOODS_EXCHANGE).durable(true).build();
    }

    /**
     * 减库存队列 order -> goods
     *
     * @return
     */
    @Bean(ORDER_GOODS_CUT_STOCK_QUEUE)
    public Queue cutStockQueueDeclare() {
        Queue queue = new Queue(ORDER_GOODS_CUT_STOCK_QUEUE, true, false, true);
        return queue;
    }

    /**
     * 减库存完成队列 goods -> order
     *
     * @return
     */
    @Bean(ORDER_GOODS_CUT_STOCK_FINISH_QUEUE)
    public Queue finishCutStockQueueDaclare() {
        Queue queue = new Queue(ORDER_GOODS_CUT_STOCK_FINISH_QUEUE, true, false, true);
        return queue;
    }

    /**
     * 绑定减库存队列到交换机
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingCutTaskQueue(@Qualifier(ORDER_GOODS_CUT_STOCK_QUEUE) Queue queue, @Qualifier(ORDER_GOODS_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ORDER_GOODS_CUT_STOCK_KEY).noargs();
    }

    /**
     * 绑定完成减库存队列到交换机
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingFinishTaskQueue(@Qualifier(ORDER_GOODS_CUT_STOCK_FINISH_QUEUE) Queue queue, @Qualifier(ORDER_GOODS_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ORDER_GOODS_CUT_STOCK_FINISH_KEY).noargs();
    }
}
