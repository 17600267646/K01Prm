package com.msb.k01Prm.controller;

import com.msb.k01Prm.service.ProductGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/product-group")
public class ProductGroupController {

    private final ProductGroupService productGroupService;

    public ProductGroupController(ProductGroupService productGroupService) {
        this.productGroupService = productGroupService;
    }

    /**
     *
     *
     *
     * 返回 product_group 表的行数
     * GET http://localhost:8081/api/product-group/count
     */
    @GetMapping("/count")
    public Map<String, Object> count() {
        long count = productGroupService.countAll();

        log.info("Query product_group count, result={}", count);

        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        return result;
    }

    /**
     * 使用 Redis（Lettuce 简单 KV）做缓存示例
     * GET http://localhost:8081/api/product-group/count-cache
     */
    @GetMapping("/count-cache")
    public Map<String, Object> countWithCache() {
        long count = productGroupService.countAllWithCache();
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("source", "redis-cache-or-db");
        return result;
    }

    /**
     * 使用 Redisson 分布式锁保护统计逻辑示例
     * GET http://localhost:8081/api/product-group/count-lock
     */
    @GetMapping("/count-lock")
    public Map<String, Object> countWithLock() {
        long count = productGroupService.countAllWithLock();
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("lock", "redisson");
        return result;
    }

    /**
     *
     */

    @GetMapping("/forHash")
    public Map<Object, Object> forHash() {
        Map<Object, Object> result = productGroupService.forHash();
        return result;
    }
}

