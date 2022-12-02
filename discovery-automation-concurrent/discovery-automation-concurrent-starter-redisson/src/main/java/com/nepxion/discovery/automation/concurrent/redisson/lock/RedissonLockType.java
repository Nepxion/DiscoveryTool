package com.nepxion.discovery.automation.concurrent.redisson.lock;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.automation.concurrent.redisson.constant.RedissonConstant;

public enum RedissonLockType {
    // 非公平锁
    NONFAIR(RedissonConstant.NONFAIR),

    // 公平锁
    FAIR(RedissonConstant.FAIR),

    // 读锁
    READ(RedissonConstant.READ),

    // 写锁
    WRITE(RedissonConstant.WRITE),

    // 自旋锁
    SPIN(RedissonConstant.SPIN);

    private String value;

    private RedissonLockType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RedissonLockType fromString(String value) {
        for (RedissonLockType type : RedissonLockType.values()) {
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