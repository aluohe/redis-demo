package com.examplerds.demo.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aluohe
 * @className RedisClient
 * @projectName demo
 * @date 2020/6/10 22:40
 * @description
 * @modified_by
 * @version:
 */
@Component
public class RedisClient<K, V> extends RedisTemplate<K, V> {

    private static final Map<String, RedisConnectionFactory> map = new HashMap<>();

    static {
        LettuceConnectionFactory factory = lettuceConnectionFactory(1);
        factory.afterPropertiesSet();
        LettuceConnectionFactory factory1 = lettuceConnectionFactory(4);
        factory1.afterPropertiesSet();
        map.put("aluohe", factory);
        map.put("wdd", factory1);

    }

    private static ThreadLocal<String> local = new ThreadLocal<>();

    public static void setLocal(String msg) {
        local.set(msg);
    }

    public static void remove() {
        local.remove();
    }

    @Override
    public RedisConnectionFactory getConnectionFactory() {
//        return super.getConnectionFactory();
        if (local.get() == null) {
            return map.get("aluohe");
        } else {
            return map.get(local.get());
        }
    }

   /* @Override
    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        super.setConnectionFactory(connectionFactory);
        map.put("aluohe", connectionFactory);
    }*/

    static LettuceConnectionFactory lettuceConnectionFactory(Integer dbIndex) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(dbIndex);
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(6379);
        redisStandaloneConfiguration.setPassword(RedisPassword.of("123456"));

        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfigurationBuilder.build());
        return factory;
    }
}
