package com.examplerds.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author aluohe
 * @className RedisConfig
 * @projectName demo
 * @date 2020/6/10 22:43
 * @description
 * @modified_by
 * @version:
 */
@Configuration
public class RedisConfig {

  /*  @Bean
    public RedisClient<String,Object> client(RedisConnectionFactory lettuceConnectionFactory){
        RedisClient client=new RedisClient();
        client.setConnectionFactory(lettuceConnectionFactory);
        client.setKeySerializer(new StringRedisSerializer());
        client.setValueSerializer(new StringRedisSerializer());
        return client;

    }*/

    @Autowired
    LettuceConnectionFactory lettuceConnectionFactory;


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisClient client = new RedisClient();
        client.setDefaultRedisFactory(lettuceConnectionFactory);
        addMultiDataSource(client);
        client.setKeySerializer(new StringRedisSerializer());
        client.setValueSerializer(new StringRedisSerializer());
        return client;

    }

    private void addMultiDataSource(RedisClient client) {
        LettuceConnectionFactory factory = lettuceConnectionFactory(1, "localhost", RedisPassword.none());
        factory.afterPropertiesSet();
        LettuceConnectionFactory factory1 = lettuceConnectionFactory(4, "192.168.2.31", RedisPassword.of("123456"));
        factory1.afterPropertiesSet();
        client.addFactory("aluohe", factory);
        client.addFactory("wdd", factory1);
        //默认数据源 可加可不加
        client.addFactory("default", lettuceConnectionFactory);
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


   /* @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(1);
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(6379);
        redisStandaloneConfiguration.setPassword(RedisPassword.of("123456"));

        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfigurationBuilder.build());
        return factory;
    }*/


}
