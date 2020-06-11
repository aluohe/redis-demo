package com.examplerds.demo.redis;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @author HuangYinQiang
 * @version 1.0
 * @Package com.kamowl.kamo.cloud.base.redis.serializer
 * @Description: redis工具类
 * @date 2019/9/24 17:15
 */
@Slf4j
@Component
public class RedisUtil {

    private static GenericJackson2JsonRedisSerializer redisObjectSerializer = new GenericJackson2JsonRedisSerializer();
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * redis工具类
     *
     * @param redisTemplate
     */
//    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }


    /*********************************************************************************普通key,value判断，过期时间，删除，设置，获取  start**********************/
    /**
     * 是否存在key
     *
     * @param key
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(key.getBytes()));
    }


    /**
     * redis根据key获取value
     *
     * @param key key
     * @return value
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public Set keys() {
        return redisTemplate.keys("*");
    }

    /**
     * redis根据key获取value（序列化对象）
     *
     * @param key
     * @return
     */
    public Object getObject(String key) {
        Object obj = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] temp = "".getBytes();
                temp = connection.get(key.getBytes());
                connection.close();
                return redisObjectSerializer.deserialize(temp);
            }
        });
        return obj;
    }


    /**
     * redis存入数据
     *
     * @param key   key
     * @param value 值
     * @return 是否成功
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.execute((RedisCallback<Long>) e -> {
                byte[] values = redisObjectSerializer.serialize(value);
                e.set(key.getBytes(), values);
                e.close();
                return 1L;
            });
            return true;
        } catch (Exception e) {
            log.error("redis存入数据发生错误:key={}，value={}", key, value);
            return false;
        }
    }


    /**
     * 存入数据
     *
     * @param key   key
     * @param value 值
     * @param time  时间/秒，0相当永久
     * @return 是否成功
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.execute((RedisCallback<Long>) connection -> {
                    byte[] values = redisObjectSerializer.serialize(value);
                    connection.set(key.getBytes(), values);
                    connection.expire(key.getBytes(), time);
                    connection.close();
                    return 1L;
                });
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis存入数据发生错误:key={}，value={}，time={}", key, value, time);
            return false;
        }
    }


    /**
     * 删除key
     *
     * @param keys
     */
    @SuppressWarnings("unchecked")
  /*  public void del(String... keys) {
        if (ArrayUtil.isNotEmpty(keys)) {
            redisTemplate.delete(CollectionUtils.arrayToList(keys));
        }
    }*/


    /**
     * 获取key的过期时间
     *
     * @param key key
     * @return 秒, 0为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.execute((RedisCallback<Long>) conn -> {
            try {
                return conn.pTtl(key.getBytes(), TimeUnit.SECONDS);
            } catch (Exception e) {
                return conn.ttl(key.getBytes(), TimeUnit.SECONDS);
            }
        });
    }


    /**
     * 设置缓存失效时间
     *
     * @param key  key
     * @param time 时间(秒)
     * @return 是否成功
     */
    public boolean expire(String key, long time) {
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> {
            long rawTimeout = TimeoutUtils.toMillis(time, TimeUnit.SECONDS);
            try {
                return conn.pExpire(key.getBytes(), rawTimeout);
            } catch (Exception e) {
                return conn.expire(key.getBytes(), TimeoutUtils.toSeconds(rawTimeout, TimeUnit.SECONDS));
            }
        });
    }
    /*********************************************************************************普通key,value判断，过期时间，删除，设置，获取  end**********************/


    /*********************************************************************************key，value增量  start**********************/
    /**
     * 增量
     *
     * @param key    键
     * @param number 增量>0
     * @return 结果
     */
   /* public long incr(String key, long number) {
        if (number < 0) {
            throw new RuntimeException(StrUtil.format("增量必须大于0,number={}", number));
        }
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.incrBy(key.getBytes(), number));
    }*/


    /**
     * 减量
     *
     * @param key    键
     * @param number 减量>0
     * @return 结果
     */
   /* public long decr(String key, long number) {
        if (number < 0) {
            throw new RuntimeException(StrUtil.format("增量必须大于0,number={}", number));
        }
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.incrBy(key.getBytes(), -number));
    }*/
    /*********************************************************************************key，value增量  end**********************/


    /*********************************************************************************偏移量  start**********************/
    /**
     * bit
     *
     * @param key    key
     * @param offset 偏移量
     * @return 是否成功
     */
    public boolean setBit(String key, long offset, boolean isShow) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().setBit(key, offset, isShow);
            result = true;
        } catch (Exception e) {
            log.error("redis存入数据发生错误:key={}，offset={}，isShow={}", key, offset, isShow);
            return false;
        }
        return result;
    }


    /**
     * bit
     *
     * @param key    key
     * @param offset 偏移量
     * @return 结果
     */
    public boolean getBit(String key, long offset) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().getBit(key, offset);
        } catch (Exception e) {
            log.error("redis存入数据发生错误:key={}，offset={}", key, offset);
            return false;
        }
        return result;
    }
    /*********************************************************************************偏移量  end**********************/


    /*********************************************************************************list  相关操作 start**********************/
    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return list
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("redis获取list数据发生错误:key={}，start={},end={}", key, start, end);
            return null;
        }
    }


    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 集合长度
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("redis获取list.size发生错误:key={}", key);
            return 0;
        }
    }


    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  索引可循环
     * @return 对象
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("redis根据下标获取list数据发生错误:key={}，index={}", key, index);
            return null;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 存入结果
     */
    public boolean lSet(String key, List value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis存入list数据发生错误:key={}", key);
            return false;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis存入list数据发生错误:key={}，time={}", key, time);
            return false;
        }
    }


    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 更新结果
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("redis根据索引修改list中的某条数据发生错误:key={}，index={}", key, index);
            return false;
        }
    }
/*********************************************************************************list  相关操作 end**********************/


/*********************************************************************************哈希  相关操作 start**********************/
    /**
     * 哈希 添加
     *
     * @param key     key
     * @param hashKey hashkey
     * @param value   对应的值
     */
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }


    /**
     * 哈希获取数据
     *
     * @param key     key
     * @param hashKey hashkey
     * @return 对应的值
     */
    public Object hGet(String key, Object hashKey) {
        Object o = redisTemplate.opsForHash().get(key, hashKey);
        return o;
    }

    /**
     * 哈希获取所有数据
     *
     * @param key key
     */
    public Map hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 哈希获取所有数据
     *
     * @param key key
     */
    public Set hGetAllKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }


    /**
     * 哈希删除
     *
     * @param key     key
     * @param hashKey hashkey
     * @return 对应的值
     */
    public void hDelete(String key, Object hashKey) {
        HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        if (stringObjectObjectHashOperations.hasKey(key, hashKey)) {
            stringObjectObjectHashOperations.delete(key, hashKey);
        }
    }
/*********************************************************************************哈希  相关操作 end**********************/


}
