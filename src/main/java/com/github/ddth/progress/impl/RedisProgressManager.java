package com.github.ddth.progress.impl;

import com.github.ddth.commons.utils.SerializationUtils;
import com.github.ddth.progress.IProgressManager;
import com.github.ddth.progress.ProgressRecord;
import com.github.ddth.redis.IRedisClient;
import com.github.ddth.redis.PoolConfig;
import com.github.ddth.redis.RedisClientFactory;

/**
 * Redis-backed implementation of {@link IProgressManager}.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.2.0
 */
public class RedisProgressManager extends AbstractProgressManager {

    public final static String COUNTER_REDIS_KEY = "_PROGRESS_MANAGER_COUNTER_";
    public final static String KEY_PREFIX = "_PM_";
    public final static int KEY_TTL = 3600;

    private RedisClientFactory redisClientFactory;
    private String redisHost, redisUser, redisPassword;
    private int redisPort;
    private PoolConfig redisPoolConfig;

    protected RedisClientFactory getRedisClientFactory() {
        return redisClientFactory;
    }

    public RedisProgressManager setRedisClientFactory(RedisClientFactory redisClientFactory) {
        this.redisClientFactory = redisClientFactory;
        return this;
    }

    protected String getRedisHost() {
        return redisHost;
    }

    public RedisProgressManager setRedisHost(String redisHost) {
        this.redisHost = redisHost;
        return this;
    }

    protected int getRedisPort() {
        return redisPort;
    }

    public RedisProgressManager setRedisPort(int redisPort) {
        this.redisPort = redisPort;
        return this;
    }

    protected String getRedisUser() {
        return redisUser;
    }

    public RedisProgressManager setRedisUser(String redisUser) {
        this.redisUser = redisUser;
        return this;
    }

    protected String getRedisPassword() {
        return redisPassword;
    }

    public RedisProgressManager setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
        return this;
    }

    protected PoolConfig getRedisPoolConfig() {
        return redisPoolConfig;
    }

    public RedisProgressManager setRedisPoolConfig(PoolConfig redisPoolConfig) {
        this.redisPoolConfig = redisPoolConfig;
        return this;
    }

    protected IRedisClient getRedisClient() {
        return redisClientFactory.getRedisClient(redisHost, redisPort, redisUser, redisPassword,
                redisPoolConfig);
    }

    protected void returnRedisClient(IRedisClient redisClient) {
        redisClientFactory.returnRedisClient(redisClient);
    }

    /**
     * Serializes a {@link ProgressRecord}.
     * 
     * @param pr
     * @return
     */
    protected byte[] serializeRecord(ProgressRecord pr) {
        return SerializationUtils.toByteArray(pr);
    }

    /**
     * Deserializes a {@link ProgressRecord}.
     * 
     * @param data
     * @return
     */
    protected ProgressRecord deserializeRecord(byte[] data) {
        return SerializationUtils.fromByteArray(data, ProgressRecord.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressRecord create() {
        IRedisClient redisClient = getRedisClient();
        try {
            long lId = redisClient.incBy(COUNTER_REDIS_KEY, 1);
            String id = Long.toHexString(lId).toLowerCase();
            ProgressRecord record = new ProgressRecord();
            record.id(id);

            String redisKey = KEY_PREFIX + id;
            byte[] data = serializeRecord(record);
            redisClient.set(redisKey, data, KEY_TTL);

            return record;
        } finally {
            returnRedisClient(redisClient);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressRecord update(ProgressRecord record) {
        IRedisClient redisClient = getRedisClient();
        try {
            String id = record.id();
            String redisKey = KEY_PREFIX + id;
            byte[] data = serializeRecord(record);
            redisClient.set(redisKey, data, KEY_TTL);

            return record;
        } finally {
            returnRedisClient(redisClient);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressRecord get(String id) {
        IRedisClient redisClient = getRedisClient();
        try {
            String redisKey = KEY_PREFIX + id;
            byte[] data = redisClient.getAsBinary(redisKey);
            return data != null ? deserializeRecord(data) : null;
        } finally {
            returnRedisClient(redisClient);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String id) {
        IRedisClient redisClient = getRedisClient();
        try {
            String redisKey = KEY_PREFIX + id;
            redisClient.delete(redisKey);
        } finally {
            returnRedisClient(redisClient);
        }
    }
}
