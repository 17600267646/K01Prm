package com.msb.k01Prm.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * RocketMQ 配置：
 * - 声明式创建 Topic：test_zmt、test_delayed，各 1 个队列
 */
@Slf4j
@Configuration
public class RocketMQConfig {

    public static final String TOPIC_TEST_ZMT = "test_zmt";
    public static final String TOPIC_TEST_DELAYED = "test_delayed";
    private static final int QUEUE_NUMS = 1;
    private static final String CLUSTER_NAME = "DefaultCluster";

    @Value("${rocketmq.name-server:127.0.0.1:9876}")
    private String nameServerAddr;

    private DefaultMQAdminExt adminExt;

    @Bean
    public DefaultMQAdminExt defaultMQAdminExt() {
        DefaultMQAdminExt admin = new DefaultMQAdminExt();
        admin.setNamesrvAddr(nameServerAddr);
        try {
            admin.start();
            createTopicIfNotExists(admin, TOPIC_TEST_ZMT);
            createTopicIfNotExists(admin, TOPIC_TEST_DELAYED);
        } catch (Exception e) {
            log.warn("RocketMQ Admin 启动或创建 Topic 失败（可能 Broker 未就绪），将依赖自动创建: {}", e.getMessage());
        }
        this.adminExt = admin;
        return admin;
    }

    private void createTopicIfNotExists(DefaultMQAdminExt admin, String topic) {
        try {
            admin.createTopic(CLUSTER_NAME, topic, QUEUE_NUMS);
            log.info("RocketMQ Topic 创建成功: {} (队列数: {})", topic, QUEUE_NUMS);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("already exist")) {
                log.debug("Topic 已存在: {}", topic);
            } else {
                log.warn("创建 Topic 失败 {}: {}", topic, e.getMessage());
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (adminExt != null) {
            adminExt.shutdown();
        }
    }
}
