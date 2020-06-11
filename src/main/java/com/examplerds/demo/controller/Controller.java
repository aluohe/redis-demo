package com.examplerds.demo.controller;

import com.examplerds.demo.aop.RedisSource;
import com.examplerds.demo.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;


    @GetMapping("/redis-test")
    @RedisSource
    public void test() {


     /*   try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("sleep out");*/

//        Set keys = redisTemplate.keys("*");
        Set keys = redisUtil.keys();
        System.out.println(keys);

    }

    @GetMapping("/redis-test1")
    @RedisSource("aluohe")
    public void test1() {

//        Set keys = redisTemplate.keys("*");

        Set keys = redisUtil.keys();

        System.out.println(keys);

    }

    @GetMapping("/redis-test2")
    @RedisSource("wdd")
    public void test2() {
//        Set keys = redisTemplate.keys("*");
        Set keys = redisUtil.keys();

        System.out.println(keys);

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
