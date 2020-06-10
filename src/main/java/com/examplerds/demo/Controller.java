package com.examplerds.demo;

import com.examplerds.demo.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author aluohe
 * @className Controller
 * @projectName demo
 * @date 2020/6/10 22:50
 * @description
 * @modified_by
 * @version:
 */
@RestController
public class Controller {

    @Autowired
    RedisClient client;

    @GetMapping("/redis-test")
    public void test() {

        client.setValueSerializer(new StringRedisSerializer());
        client.setKeySerializer(new StringRedisSerializer());
//        LettuceConnectionFactory factory = lettuceConnectionFactory();
//        factory.afterPropertiesSet();
//        factory.resetConnection();

//        client.setConnectionFactory(factory);
        Set keys =client.keys("*");
        System.out.println(keys);

        RedisClient.setLocal("wdd");


        Set keys1 = client.keys("*");


        System.out.println(keys1);
    }

   /* LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(0);
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(6379);
        redisStandaloneConfiguration.setPassword(RedisPassword.of("123456"));

        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfigurationBuilder.build());
        return factory;
    }*/


}
