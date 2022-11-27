package com.nepxion.discovery.automation.concurrent.redisson.processor;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public enum RedissonLockType {
    // 普通锁
    LOCK("Lock"),

    // 读锁
    READ_LOCK("ReadLock"),

    // 写锁
    WRITE_LOCK("WriteLock");

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