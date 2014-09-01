package com.github.ddth.progress.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.github.ddth.progress.ProgressRecord;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class InprocessProgressManager extends AbstractProgressManager {

    private final AtomicLong counter = new AtomicLong(0);
    private final Cache<String, ProgressRecord> progressCache = CacheBuilder.newBuilder()
            .expireAfterAccess(3600, TimeUnit.SECONDS).build();

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressRecord create() {
        String id = Long.toHexString(counter.incrementAndGet()).toLowerCase();
        ProgressRecord record = new ProgressRecord();
        record.id(id);
        progressCache.put(id, record);
        return record;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressRecord update(ProgressRecord record) {
        String id = record.id();
        progressCache.put(id, record);
        return record;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressRecord get(String id) {
        return progressCache.getIfPresent(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String id) {
        progressCache.invalidate(id);
    }
}
