package com.transaction.mq.domain;

/**
 * @category 商品减库存任务
 * @author Ellen
 * @date 2020
 * @description
 */
public class GoodsCutTask {
    private Integer id;
    private Integer order_id;
    private Integer task_id;
    private byte task_type;
    private byte version;
    private byte status;
    private String mq_exchange;
    private String mq_routing_key;
    private String msg_content;
    private Integer create_time;
    private Integer update_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public byte getTask_type() {
        return task_type;
    }

    public void setTask_type(byte task_type) {
        this.task_type = task_type;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getMq_exchange() {
        return mq_exchange;
    }

    public void setMq_exchange(String mq_exchange) {
        this.mq_exchange = mq_exchange;
    }

    public String getMq_routing_key() {
        return mq_routing_key;
    }

    public void setMq_routing_key(String mq_routing_key) {
        this.mq_routing_key = mq_routing_key;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public Integer getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Integer create_time) {
        this.create_time = create_time;
    }

    public Integer getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Integer update_time) {
        this.update_time = update_time;
    }
}
