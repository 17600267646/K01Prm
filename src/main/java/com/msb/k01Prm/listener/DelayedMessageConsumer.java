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

/**
 * 延迟队列消费者：
 * - 监听队列：test_delayed
 * - 消费时打印日志（包含当前时间）并手动 ack
 */
@Slf4j
@Component
public class DelayedMessageConsumer {

    @RabbitListener(queues = RabbitConfig.DELAY_QUEUE)
    public void handleDelayedMessage(String msg, Channel channel, Message message) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("msg", msg);
            logData.put("time", DateUtil.now());

            log.info("Receive DELAYED from RabbitMQ: {}", JSON.toJSONString(logData));

            // 正常处理完成，手动确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Consume DELAYED message error, will NACK and requeue. msg={}", msg, e);
            // 业务异常，NACK 并选择重回队列
            channel.basicNack(deliveryTag, false, true);
        }
    }
}

