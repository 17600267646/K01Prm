package com.msb.k01Prm.listener;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.msb.k01Prm.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ 消费者：
 * - 监听 test_queue_two
 * - 消费时打印：参数 + 当前时间（以 JSON 日志方式）
 */
@Slf4j
@Component
public class RabbitTestConsumer {

    @RabbitListener(queues = RabbitConfig.TEST_QUEUE)
    public void handleMessage(String msg, Channel channel, Message message) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            TimeUnit.SECONDS.sleep(1);
            Map<String, Object> logData = new HashMap<>();
            logData.put("msg", msg);
            logData.put("time", DateUtil.now());

            log.info("Receive from RabbitMQ: {}", JSON.toJSONString(logData));

            // 业务正常，手动确认消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Consume message error, will NACK and requeue. msg={}", msg, e);
            // 业务异常，手动 NACK，可根据需要选择是否重回队列
            channel.basicNack(deliveryTag, false, true);
        }
    }
}

