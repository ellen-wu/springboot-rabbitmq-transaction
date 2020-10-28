package com.transaction.mq.service;

import com.alibaba.fastjson.JSON;
import com.transaction.mq.domain.OrderCutTask;
import com.transaction.mq.domain.OrderCutTaskHistory;
import com.transaction.mq.mapper.OrderCutTaskHistoryMapper;
import com.transaction.mq.mapper.OrderCutTaskMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TaskService {
    @Autowired
    private OrderCutTaskMapper taskMapper;

    @Autowired
    private OrderCutTaskHistoryMapper historyMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查询任务列表
     * 实际中应该控制查询得条数
     * 这里只是为了测试，不做过多处理
     *
     * @param updateTime
     * @return
     */
    public List<OrderCutTask> findWaiteDoTask(Integer updateTime) {
        return taskMapper.queryWaiteCutStockTask(updateTime);
    }

    // 获取对应id和版本得任务，并更新version
    @Transactional
    public int getTaskByIdAndVersion(Integer id, byte version) {
        Integer updateCount = taskMapper.updateVerisonByIdAndVersion(id, version);
        return updateCount;
    }

    /**
     * 发送减库存的消息
     *
     * @param task
     * @param exchange
     * @param routingKey
     */
    public void sendCutStockMsg(OrderCutTask task, String exchange, String routingKey) {
        OrderCutTask taskExists = taskMapper.queryById(task.getId());
        if (taskExists != null) {
            // 向rabbitmq 发送减库存消息
            rabbitTemplate.convertAndSend(exchange, routingKey, JSON.toJSONString(task));
            // 更新任务时间
            Integer timeNow = Math.toIntExact(System.currentTimeMillis() / 1000);
            taskMapper.updateTimeById(task.getId(), timeNow);
        }
    }

    @Transactional
    public void finishTask(Integer taskId) {
        OrderCutTask taskExists = taskMapper.queryById(taskId);
        if (taskExists != null) {
            Integer timeNow = Math.toIntExact(System.currentTimeMillis() / 1000);
            // 新增任务历史记录
            OrderCutTaskHistory taskHistory = new OrderCutTaskHistory();
            taskHistory.setOrder_id(taskExists.getOrder_id());
            taskHistory.setTask_id(taskExists.getId());
            taskHistory.setTask_type(taskExists.getTask_type());
            taskHistory.setVersion(taskExists.getVersion());
            taskHistory.setStatus(taskExists.getStatus());
            taskHistory.setMq_exchange(taskExists.getMq_exchange());
            taskHistory.setMq_routing_key(taskExists.getMq_routing_key());
            taskHistory.setMsg_content(taskExists.getMsg_content());
            taskHistory.setError_msg(taskExists.getError_msg());
            taskHistory.setCreate_time(taskExists.getCreate_time());
            taskHistory.setUpdate_time(taskExists.getUpdate_time());

            historyMapper.insertOne(taskHistory);

            // 删除任务
            taskMapper.deleteById(taskId);
        }
    }

}
