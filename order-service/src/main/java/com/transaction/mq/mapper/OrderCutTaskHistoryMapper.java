package com.transaction.mq.mapper;

import com.transaction.mq.domain.OrderCutTaskHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface OrderCutTaskHistoryMapper {

    @Insert("insert into `order_cut_task_history` (`order_id`, `task_id`, `task_type`, `version`, `status`, " +
            "`mq_exchange`, `mq_routing_key`, `msg_content`, `error_msg`, `create_time`, `update_time`) values " +
            "(#{order_id}, #{task_id}, #{task_type}, #{version}, #{status}, #{mq_exchange}, #{mq_routing_key}, " +
            "#{msg_content}, #{error_msg}, #{create_time}, #{update_time})")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = int.class)
    void insertOne(OrderCutTaskHistory orderCutTaskHistory);
}
