package com.msb.k01Prm.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.msb.k01Prm.config.RocketMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * RocketMQ 测试接口：
 * - 将请求参数发送到主题 test_zmt
 * - 将请求参数发送到延时主题 test_delayed，延时 60s 消费
 */
@Slf4j
@RestController
@RequestMapping("/api/rocketmq")
public class RocketMqTestController {

    private final RocketMQTemplate rocketMQTemplate;

    /** RocketMQ 延时级别：5 = 1 分钟（60 秒） */
    private static final int DELAY_LEVEL_60S = 5;

    public RocketMqTestController(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    //# 普通消息
    //curl -X POST "http://localhost:8081/api/rocketmq/send?msg=hello_zmt"

    /**
     * 发送到主题 test_zmt。
     * 示例：POST /api/rocketmq/send?msg=hello  或  POST /api/rocketmq/send  Body: {"msg":"hello"}
     */
    @PostMapping("/send")
    public Map<String, Object> send(@RequestParam(value = "msg", required = false) String msg,
                                    @RequestBody(required = false) String body) {
        String content = msg != null ? msg : (body != null ? body : "");
        Map<String, Object> logData = new HashMap<>();
        logData.put("msg", content);
        logData.put("time", DateUtil.now());
        log.info("RocketMQ send to test_zmt: {}", JSON.toJSONString(logData));

        rocketMQTemplate.syncSend(RocketMQConfig.TOPIC_TEST_ZMT, content, 3000);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("topic", RocketMQConfig.TOPIC_TEST_ZMT);
        result.put("msg", content);
        return result;
    }

    /**
     * 发送到延时主题 test_delayed，延时 60 秒后消费。
     * 示例：POST /api/rocketmq/sendDelayed?msg=hello_delay
     *
     */
    //# 延时 60s 消息
    //curl -X POST "http://localhost:8081/api/rocketmq/sendDelayed?msg=hello_delayed"
    @PostMapping("/sendDelayed")
    public Map<String, Object> sendDelayed(@RequestParam(value = "msg", required = false) String msg,
                                           @RequestBody(required = false) String body) {
        String content = msg != null ? msg : (body != null ? body : "");
        Map<String, Object> logData = new HashMap<>();
        logData.put("msg", content);
        logData.put("time", DateUtil.now());
        logData.put("delayLevel", DELAY_LEVEL_60S);
        logData.put("delaySeconds", 60);
        log.info("RocketMQ send DELAYED to test_delayed: {}", JSON.toJSONString(logData));

        rocketMQTemplate.syncSend(
                RocketMQConfig.TOPIC_TEST_DELAYED,
                MessageBuilder.withPayload(content).build(),
                3000,
                DELAY_LEVEL_60S
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("topic", RocketMQConfig.TOPIC_TEST_DELAYED);
        result.put("delaySeconds", 60);
        result.put("msg", content);
        return result;
    }
}
