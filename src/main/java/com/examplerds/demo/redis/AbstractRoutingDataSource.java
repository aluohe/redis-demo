package com.examplerds.demo.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aluohe
 * @className AbstractRoutingDataSource
 * @projectName demo
 * @date 2020/6/11 10:41
 * @description
 * @modified_by
 * @version:
 */
public abstract class AbstractRoutingDataSource<K, V> extends RedisTemplate<K, V> {

    private RedisConnectionFactory defaultRedisFactory;

    private Map<String, RedisConnectionFactory> factoryMap;

    protected static final Map<String, RedisConnectionFactory> resolvedDataSources = new ConcurrentHashMap<>();

    public void setDefaultRedisFactory(RedisConnectionFactory defaultRedisFactory) {
        this.defaultRedisFactory = defaultRedisFactory;
    }

    public void setTargetRedisFactory(Map<String, RedisConnectionFactory> factoryMap) {
        this.factoryMap = factoryMap;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if (factoryMap != null && !factoryMap.isEmpty()) {
            resolvedDataSources.putAll(factoryMap);
        }
    }

    protected RedisConnectionFactory determineTargetDataSource() {
        Assert.notNull(resolvedDataSources, "DataSource router not initialized");
        String lookupKey = this.determineCurrentLookupKey();
        RedisConnectionFactory dataSource;
        if (lookupKey == null) {
            dataSource = this.defaultRedisFactory;
        } else {
            dataSource = resolvedDataSources.get(lookupKey);
            if (dataSource == null) {
                dataSource = this.defaultRedisFactory;
            }
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        } else {
            return dataSource;
        }
    }

    @Override
    public RedisConnectionFactory getConnectionFactory() {
        return determineTargetDataSource();
    }

    protected abstract String determineCurrentLookupKey();
}