package com.nepxion.discovery.automation.concurrent.redisson.processor;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.automation.concurrent.redisson.constant.RedissonConstant;

public enum RedissonLockHeldType {
    // 锁被分布式持有
    DISTRIBUTION(RedissonConstant.DISTRIBUTION),

    // 锁被本地持有
    LOCAL(RedissonConstant.LOCAL);

    private String value;

    private RedissonLockHeldType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RedissonLockHeldType fromString(String value) {
        for (RedissonLockHeldType type : RedissonLockHeldType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}