//package com.examplerds.demo.redis;
//
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author aluohe
// * @className RedisClient
// * @projectName demo
// * @date 2020/6/10 22:40
// * @description
// * @modified_by
// * @version:
// */
//
//public class RedisClient<K, V> extends AbstractRoutingDataSource<K, V> {
//
//    private Map<String, RedisConnectionFactory> factoryMap;
//
//    public RedisClient() {
//        factoryMap = new HashMap<>();
//        super.setTargetRedisFactory(factoryMap);
//    }
//
//    public <K extends String, T extends RedisConnectionFactory> void addFactory(K key, T t) {
//        factoryMap.put(key, t);
//    }
//
//
//    @Override
//    public String determineCurrentLookupKey() {
//        return RedisSourceContextHolder.get();
//    }
//}
