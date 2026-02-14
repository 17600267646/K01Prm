package com.msb.k01Prm.listener;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.msb.k01Prm.config.RocketMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RocketMQ 延时队列消费者：监听主题 test_delayed，消费时打印日志（约 60s 后投递）。
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = RocketMQConfig.TOPIC_TEST_DELAYED,
        consumerGroup = "g_test_delayed"
)
public class TestDelayedConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("topic", RocketMQConfig.TOPIC_TEST_DELAYED);
        logData.put("msg", message);
        logData.put("time", DateUtil.now());
        log.info("RocketMQ 消费 test_delayed（延时）: {}", JSON.toJSONString(logData));
    }
}
