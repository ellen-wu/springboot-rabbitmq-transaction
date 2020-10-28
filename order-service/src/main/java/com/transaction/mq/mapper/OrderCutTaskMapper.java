package com.transaction.mq.mapper;

import com.transaction.mq.domain.OrderCutTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderCutTaskMapper {
    /**
     * 插入一条需要减库存的记录
     *
     * @param task
     */
    @Insert("insert into `order_cut_task` (`order_id`, `task_type`, `version`, `status`, `mq_exchange`, " +
            "`mq_routing_key`, `msg_content`, `error_msg`, `create_time`, `update_time`) values (#{order_id}, " +
            "#{task_type}, #{version}, #{status}, #{mq_exchange}, #{mq_routing_key}, #{msg_content}, #{error_msg}, " +
            "#{create_time}, #{update_time})")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = int.class)
    void insertOne(OrderCutTask task);

    /**
     * 查询需要减库存的任务列表
     *
     * @param updateTime
     * @return
     */
    @Select("select * from `order_cut_task` where `update_time` > #{updateTime}")
    List<OrderCutTask> queryWaiteCutStockTask(Integer updateTime);

    /**
     * 根据任务id和版本更新任务的版本
     *
     * @param id
     * @return
     */
    @Update("update `order_cut_task` set `version` = `version` + 1 where id=#{id} and `version`=#{version}")
    Integer updateVerisonByIdAndVersion(Integer id, byte version);

    /**
     * 任务执行了，更新任务时间
     *
     * @param id
     * @param updateTime
     * @return
     */
    @Update("update `order_cut_task` set `update_time` = #{updateTime} where id=#{id}")
    Integer updateTimeById(Integer id, Integer updateTime);

    /**
     * 根据任务id查找任务
     * @param id
     * @return
     */
    @Select("select * from `order_cut_task` where id=#{id}")
    OrderCutTask queryById(Integer id);

    /**
     * 根据任务id删除任务
     * @param id
     * @return
     */
    @Delete("delete from `order_cut_task` where id=#{id}")
    Integer deleteById(Integer id);
}
