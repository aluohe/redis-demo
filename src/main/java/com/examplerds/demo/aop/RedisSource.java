package com.examplerds.demo.aop;

import java.lang.annotation.*;

/**
 * @author aluohe
 * @className RedisSource
 * @projectName demo
 * @date 2020/6/11 14:16
 * @description
 * @modified_by
 * @version:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisSource {

    String value() default "default";
}
