package com.transaction.mq.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.transaction.mq.config.RabbitMqConfig;
import com.transaction.mq.domain.OrderCutTask;
import com.transaction.mq.service.TaskService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @category 减库存任务类
 * @author Ellen
 * @date 2020
 * @description 包含定时执行减库存任务和任务完成消费者
 */
@Component
public class CutStockTask {
    @Autowired
    private TaskService taskService;

    /**
     * 定时任务发送减库存消息
     */
    @Scheduled(cron = "*/3 * * * * *")
    public void sendCutStockTask() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(GregorianCalendar.HOUR, -10);
        Integer updateTime = Math.toIntExact(calendar.getTimeInMillis() / 1000);

        List<OrderCutTask> taskList = taskService.findWaiteDoTask(updateTime);
        for (OrderCutTask task : taskList) {
            // 取得任务，任务version+1
            if (taskService.getTaskByIdAndVersion(task.getId(), task.getVersion()) > 0) {
                // 向mq发送减库存的消息
                taskService.sendCutStockMsg(task, task.getMq_exchange(), task.getMq_routing_key());
            }
        }
    }

    /**
     * 减库存任务完成
     * 新增历史任务（相当于转移任务到历史任务表）
     * 删除任务
     * @param jsonString
     */
    @RabbitListener(queues = RabbitMqConfig.ORDER_GOODS_CUT_STOCK_FINISH_QUEUE)
    public void finishCutStockTaskConsumer(String jsonString) {
        OrderCutTask task = JSON.parseObject(jsonString, OrderCutTask.class);
        taskService.finishTask(task.getId());
        System.out.println("任务[" + task.getId() + "]完成了！");
    }
}
