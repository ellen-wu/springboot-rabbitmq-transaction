package com.transaction.mq.mapper;

import com.transaction.mq.domain.GoodsCutTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

/**
 * @category 商品任务mapper
 * @author Ellen
 * @date 2020
 * @description
 */
@Mapper
public interface GoodsCutTaskMapper {

    /**
     * 插入一条任务
     * @param cutTask
     */
    @Insert("insert into `goods_cut_task` (`order_id`, `task_id`, `task_type`, `version`, `status`, `mq_exchange`, " +
            "`mq_routing_key`, `msg_content`, `create_time`, `update_time`) values (#{order_id}, #{task_id}, " +
            "#{task_type}, #{version}, #{status}, #{mq_exchange}, #{mq_routing_key}, #{msg_content}, " +
            "#{create_time}, #{update_time})")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = int.class)
    void insertOne(GoodsCutTask cutTask);

    /**
     * 查询任务是否已经存在
     * @param taskId
     * @return
     */
    @Select("select * from `goods_cut_task` where task_id=#{taskId}")
    GoodsCutTask queryByTaskId(Integer taskId);
}
