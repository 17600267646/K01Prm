package com.msb.k01Prm.service;

import java.util.Map;

public interface ProductGroupService {

    /**
     * 获取 product_group 表的总行数
     */
    long countAll();

    /**
     * 使用 Redis（Lettuce）缓存行数的示例：
     *  - 先从 Redis 读取 key，如果没有再查询数据库并回写 Redis
     */
    long countAllWithCache();

    /**
     * 使用 Redisson 分布式锁保护统计逻辑的示例
     */
    long countAllWithLock();
    Map<Object, Object> forHash();
}

