package com.examplerds.demo.redis;

/**
 * @author aluohe
 */
public class RedisSourceContextHolder {
    

    private static final ThreadLocal<String> REDIS_DATASOURCE_CONTEXT = new ThreadLocal<>();

    public static void set(String datasourceType) {
        REDIS_DATASOURCE_CONTEXT.set(datasourceType);
    }

    public static String get() {
        return REDIS_DATASOURCE_CONTEXT.get();
    }

    public static void clear() {
        REDIS_DATASOURCE_CONTEXT.remove();
    }
}