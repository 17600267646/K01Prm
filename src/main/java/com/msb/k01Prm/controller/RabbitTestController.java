package com.msb.k01Prm.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.msb.k01Prm.enums.RabbitExchangeEnum;
import com.msb.k01Prm.enums.RabbitQueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单的 RabbitMQ 测试接口：
 * - 将请求参数发送到 test_queue_two 队列
 * - 发送时标记消息编码 UTF-8、内容类型 text/plain
 */
@Slf4j
@RestController
@RequestMapping("/api/rabbit")
public class RabbitTestController {

    private final RabbitTemplate rabbitTemplate;

    public RabbitTestController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 示例：
     * POST http://localhost:8081/api/rabbit/send?msg=hello
     */
    //使用 ab 工具测试
    //# 1. 创建空文件（仅需执行一次）
    //touch empty.txt
    //
    //# 2. 发送 10 次 POST 请求（核心命令）
    //ab -n 10 -p empty.txt -T    "application/x-www-form-urlencoded" "http://localhost:8081/api/rabbit/send?msg=hello_rabbit"




    @PostMapping("/send")
    public Map<String, Object> sendMessage(@RequestParam("msg") String msg) {
        // 日志结构化输出
        Map<String, Object> logData = new HashMap<>();
        logData.put("msg", msg);
        logData.put("time", DateUtil.now());

        log.info("Send to RabbitMQ: {}", JSON.toJSONString(logData));

        MessagePostProcessor processor = message -> {
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.name());
            message.getMessageProperties().setContentType("text/plain");
            return message;
        };

        rabbitTemplate.convertAndSend(
                RabbitExchangeEnum.DIRECT_EXCHANGE.getName(),
                RabbitQueueEnum.TEST_QUEUE.getName(),
                msg,
                processor
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("queue", RabbitQueueEnum.TEST_QUEUE.getName());
        result.put("exchange", RabbitExchangeEnum.DIRECT_EXCHANGE.getName());
        result.put("msg", msg);
        return result;
    }

    /**
     * 将字符串消息发送到延迟队列 `test_delayed`，延迟 2 分钟消费。
     *
     * 示例：
     * POST http://localhost:8081/api/rabbit/sendDelayed?msg=hello_delay
     */
    @PostMapping("/sendDelayed")
    public Map<String, Object> sendDelayedMessage(@RequestParam("msg") String msg) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("msg", msg);
        logData.put("time", DateUtil.now());
        logData.put("delayMs", 2 * 60 * 1000);

        log.info("Send DELAYED to RabbitMQ: {}", JSON.toJSONString(logData));

        MessagePostProcessor processor = message -> {
            message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.name());
            message.getMessageProperties().setContentType("text/plain");
            // 设置延迟时间（毫秒），依赖 rabbitmq_delayed_message_exchange 插件
            message.getMessageProperties().setHeader("x-delay", 2 * 60 * 1000);
            return message;
        };

        rabbitTemplate.convertAndSend(
                RabbitExchangeEnum.DELAY_EXCHANGE.getName(),
                RabbitQueueEnum.DELAY_QUEUE.getName(),
                msg,
                processor
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("queue", RabbitQueueEnum.DELAY_QUEUE.getName());
        result.put("exchange", RabbitExchangeEnum.DELAY_EXCHANGE.getName());
        result.put("delayMs", 2 * 60 * 1000);
        result.put("msg", msg);
        return result;
    }
}

