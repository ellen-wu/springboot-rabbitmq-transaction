package com.transaction.mq.service;

import com.transaction.mq.domain.GoodsCutTask;
import com.transaction.mq.domain.OrderCutTask;
import com.transaction.mq.mapper.GoodsCutTaskMapper;
import com.transaction.mq.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @category 减库存服务
 * @author Ellen
 * @date 2020
 * @description
 */
@Service
public class CutStockService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsCutTaskMapper taskMapper;

    /**
     * 减库存并增加减库存任务记录
     * @param goodsId
     * @param buyNumber
     * @param orderCutTask
     * @return
     */
    @Transactional
    public Boolean cutGoodsStock(Integer goodsId, Integer buyNumber, OrderCutTask orderCutTask) {
        GoodsCutTask goodsCutTask = taskMapper.queryByTaskId(orderCutTask.getId());
        if (goodsCutTask == null) {
            // 减库存
            Integer updateCount = goodsMapper.updateStockById(goodsId, buyNumber);

            // 当前时间 秒
            Integer timeNow = Math.toIntExact(System.currentTimeMillis() / 1000);
            // 添加到任务
            GoodsCutTask cutAddTask = new GoodsCutTask();
            cutAddTask.setOrder_id(orderCutTask.getOrder_id());
            cutAddTask.setTask_id(orderCutTask.getId());
            cutAddTask.setTask_type(orderCutTask.getTask_type());
            cutAddTask.setVersion(orderCutTask.getVersion());
            cutAddTask.setStatus(orderCutTask.getStatus());
            cutAddTask.setMq_exchange(orderCutTask.getMq_exchange());
            cutAddTask.setMq_routing_key(orderCutTask.getMq_routing_key());
            cutAddTask.setMsg_content(orderCutTask.getMsg_content());
            cutAddTask.setCreate_time(timeNow);
            cutAddTask.setUpdate_time(timeNow);

            taskMapper.insertOne(cutAddTask);

            if (updateCount > 0 && cutAddTask.getId() > 0) {
                return true;
            }
            return false;
        }

        return true;
    }
}
