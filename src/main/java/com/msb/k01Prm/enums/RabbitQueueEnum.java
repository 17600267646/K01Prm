package com.msb.k01Prm.enums;

/**
 * RabbitMQ 队列枚举
 */
public enum RabbitQueueEnum {

    /**
     * 普通测试队列
     */
    TEST_QUEUE("test_queue_two"),

    /**
     * 延迟队列
     */
    DELAY_QUEUE("test_delayed");

    private final String name;

    RabbitQueueEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

