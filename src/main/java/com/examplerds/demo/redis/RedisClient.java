package com.examplerds.demo.redis;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aluohe
 * @className RedisClient
 * @projectName demo
 * @date 2020/6/10 22:40
 * @description
 * @modified_by
 * @version:
 */

public class RedisClient<K, V> extends RedisTemplate<K, V> implements AbstractRoutingDataSource {

    private RedisConnectionFactory defaultRedisFactory;

    private static final Map<String, RedisConnectionFactory> resolvedDataSources = new ConcurrentHashMap<>();

    public void setDefaultRedisFactory(RedisConnectionFactory defaultRedisFactory) {
        this.defaultRedisFactory = defaultRedisFactory;
        resolvedDataSources.put("default", this.defaultRedisFactory);
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


    static {
        LettuceConnectionFactory factory = lettuceConnectionFactory(1, "localhost", RedisPassword.none());
        factory.afterPropertiesSet();
        LettuceConnectionFactory factory1 = lettuceConnectionFactory(4, "192.168.2.31", RedisPassword.of("123456"));
        factory1.afterPropertiesSet();
        resolvedDataSources.put("aluohe", factory);
        resolvedDataSources.put("wdd", factory1);
    }


    @Override
    public RedisConnectionFactory getConnectionFactory() {
        return determineTargetDataSource();
    }


    public static LettuceConnectionFactory lettuceConnectionFactory(Integer dbIndex, String hostName, RedisPassword password) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(dbIndex);
        redisStandaloneConfiguration.setHostName(hostName);
        redisStandaloneConfiguration.setPort(6379);
        redisStandaloneConfiguration.setPassword(password);

        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfigurationBuilder.build());
        return factory;
    }

    @Override
    public String determineCurrentLookupKey() {
        return RedisSourceContextHolder.get();
    }
}
