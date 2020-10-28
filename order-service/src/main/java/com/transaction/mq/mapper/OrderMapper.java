package com.transaction.mq.mapper;

import com.transaction.mq.domain.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface OrderMapper {
    @Insert("insert into `order` (`order_no`, `out_order_no`, `goods_id`, `buy_number`, `create_time`, `status`)" +
            " values (#{order_no}, #{out_order_no}, #{goods_id}, #{buy_number}, " +
            "#{create_time}, #{status})")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = int.class)
    void insertOne(Order order);
}
