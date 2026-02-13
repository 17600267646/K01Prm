package com.msb.k01Prm.config;

import com.msb.k01Prm.enums.RabbitExchangeEnum;
import com.msb.k01Prm.enums.RabbitQueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 基础配置：
 * - 使用内置直连交换机 amq.direct
 * - 声明测试队列 test_queue_two，并绑定到 amq.direct
 */
@Configuration
public class RabbitConfig {

    /**
     * 队列 & 交换机名称常量（如果其他地方仍然依赖字符串，可以继续使用这些常量）
     */
    // 这里必须使用「字面量」，才能作为注解中的编译期常量使用
    public static final String TEST_QUEUE = "test_queue_two";
    public static final String DIRECT_EXCHANGE = "amq.direct";

    /**
     * 延迟队列与交换机配置：
     * - 依赖 rabbitmq_delayed_message_exchange 插件
     * - 交换机名称：amp.delayed（已在管理端创建）
     * - 队列名称：test_delayed
     * - 路由键：使用队列名 test_delayed
     */
    public static final String DELAY_QUEUE = "test_delayed";
    public static final String DELAY_EXCHANGE = "amp.delayed";

    @Bean
    public Queue testQueueTwo() {
        // durable: true, exclusive: false, autoDelete: false
        return new Queue(RabbitQueueEnum.TEST_QUEUE.getName(), true, false, false);
    }

    /**
     * 延迟队列（持久化）
     */
    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(RabbitQueueEnum.DELAY_QUEUE.getName()).build();
    }

    @Bean
    public DirectExchange amqDirectExchange() {
        // amq.direct 是 RabbitMQ 内置交换机，这里按同名声明（幂等）
        return new DirectExchange(RabbitExchangeEnum.DIRECT_EXCHANGE.getName(), true, false);
    }

    /**
     * 延迟交换机（x-delayed-message）
     * 如果已经在控制台创建，名称与类型一致时会是幂等声明。
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        // 指定底层路由行为为 direct
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitExchangeEnum.DELAY_EXCHANGE.getName(), "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding testQueueTwoBinding() {
        // 这里使用队列名作为 routingKey，便于理解和路由
        return BindingBuilder.bind(testQueueTwo())
                .to(amqDirectExchange())
                .with(RabbitQueueEnum.TEST_QUEUE.getName());
    }

    /**
     * 延迟队列与延迟交换机绑定
     */
    @Bean
    public Binding delayQueueBinding() {
        return BindingBuilder.bind(delayQueue())
                .to(delayedExchange())
                .with(RabbitQueueEnum.DELAY_QUEUE.getName())
                .noargs();
    }
}

