package com.msb.k01Prm.enums;

/**
 * RabbitMQ 交换机枚举
 */
public enum RabbitExchangeEnum {

    /**
     * 内置直连交换机 amq.direct
     */
    DIRECT_EXCHANGE("amq.direct"),

    /**
     * 延迟交换机（依赖 rabbitmq_delayed_message_exchange 插件）
     */
    DELAY_EXCHANGE("amp.delayed");

    private final String name;

    RabbitExchangeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

