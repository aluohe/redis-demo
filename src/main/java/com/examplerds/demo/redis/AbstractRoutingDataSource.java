package com.examplerds.demo.redis;

/**
 * @author aluohe
 * @className AbstractRoutingDataSource
 * @projectName demo
 * @date 2020/6/11 10:41
 * @description
 * @modified_by
 * @version:
 */
public interface AbstractRoutingDataSource {

    String determineCurrentLookupKey();
}