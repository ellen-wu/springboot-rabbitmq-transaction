package com.transaction.mq.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @category 商品mapper
 * @author Ellen
 * @date 2020
 * @description
 */
@Mapper
public interface GoodsMapper {
    /**
     * 更新库存
     * @param id
     * @param cutStock
     * @return
     */
    @Update("update `goods` set `stock`=`stock` - #{cutStock} where id=#{id}")
    Integer updateStockById(Integer id, Integer cutStock);
}
