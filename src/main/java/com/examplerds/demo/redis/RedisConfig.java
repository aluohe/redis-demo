package com.examplerds.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        client.setKeySerializer(new StringRedisSerializer());
        client.setValueSerializer(new StringRedisSerializer());
        return client;

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
