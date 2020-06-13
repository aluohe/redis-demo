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

public class RedisClient<K, V> extends AbstractRoutingDataSource<K, V> {

    private Map<String, RedisConnectionFactory> factoryMap;

    public RedisClient() {
        factoryMap = new HashMap<>();
        super.setTargetRedisFactory(factoryMap);
    }

    public <K extends String, T extends RedisConnectionFactory> void addFactory(K key, T t) {
        factoryMap.put(key, t);
    }


    @Override
    public String determineCurrentLookupKey() {
        return RedisSourceContextHolder.get();
    }
}
