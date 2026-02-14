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
import java.util.concurrent.TimeUnit;

/**
 * RocketMQ 消费者：监听主题 test_zmt，消费时打印日志。
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = RocketMQConfig.TOPIC_TEST_ZMT,
        consumerGroup = "g_test_zmt"
)
public class TestZmtConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("RocketMQ 消费 start test_zmt: {} ", message);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.info("mq sleep err");
        }
        Map<String, Object> logData = new HashMap<>();
        logData.put("topic", RocketMQConfig.TOPIC_TEST_ZMT);
        logData.put("msg", message);
        logData.put("time", DateUtil.now());
        log.info("RocketMQ 消费 test_zmt: {}", JSON.toJSONString(logData));
    }
}
