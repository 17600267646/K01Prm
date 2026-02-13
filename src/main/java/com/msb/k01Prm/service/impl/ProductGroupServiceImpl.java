package com.msb.k01Prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.k01Prm.entity.ProductGroup;
import com.msb.k01Prm.mapper.ProductGroupMapper;
import com.msb.k01Prm.service.ProductGroupService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ProductGroupServiceImpl implements ProductGroupService {

    /**
     * Mapper 一般使用 @Resource 按名称注入，也完全没问题
     */
    @Resource
    private ProductGroupMapper productGroupMapper;

    /**
     * 其它 Bean 使用 @Autowired 字段注入（示例）
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public long countAll() {
        // 不带任何条件，直接统计 product_group 表行数
        return productGroupMapper.selectCount(new QueryWrapper<ProductGroup>());
    }

    @Override
    public long countAllWithCache() {
        String key = "product_group:count";

        // 1. 先从 Redis 中取（Lettuce 客户端，简单 String KV）
        String cached = stringRedisTemplate.opsForValue().get(key);
        if (cached != null) {
            return Long.parseLong(cached);
        }

        // 2. 缓存未命中，再查询数据库
        long count = countAll();

        // 3. 回写 Redis，设置一个合适的过期时间（例如 60s）
        stringRedisTemplate.opsForValue().set(key, String.valueOf(count), 60, TimeUnit.SECONDS);

        return count;
    }

    @Override
    public long countAllWithLock() {
        String lockKey = "product_group:count:lock";
        RLock lock = redissonClient.getLock(lockKey);

        boolean locked = false;
        try {
            // 尝试获取锁，最多等待 3 秒，锁自动在 10 秒后过期
            locked = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if (!locked) {
                // 获取锁失败时的降级策略：直接查询数据库或返回缓存
                return countAll();
            }

            // 在锁保护下执行统计逻辑（这里示例继续复用缓存逻辑）
            return countAllWithCache();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return countAll();
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Map<Object, Object> forHash() {
        stringRedisTemplate.opsForHash().put("testMap", "key1", "value1");
        stringRedisTemplate.opsForHash().put("testMap", "key2", "value2");
        Map<Object, Object> testMap = stringRedisTemplate.opsForHash().entries("testMap");

        return testMap;
    }
}

