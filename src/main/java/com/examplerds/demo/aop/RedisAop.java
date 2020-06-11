package com.examplerds.demo.aop;

import com.examplerds.demo.redis.RedisSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author aluohe
 * @className RedisAop
 * @projectName demo
 * @date 2020/6/11 13:54
 * @description
 * @modified_by
 * @version:
 */
@Slf4j
@Aspect
@Component
public class RedisAop {


    @Around("@annotation(com.examplerds.demo.aop.RedisSource)")
    public Object setDynamicDataSource(ProceedingJoinPoint pjp) throws Throwable {
        boolean clear = true;
        try {
            Method method = this.getMethod(pjp);
            RedisSource  dataSourceImport = method.getAnnotation(RedisSource .class);
//            clear = dataSourceImport.clear();
            RedisSourceContextHolder.set(dataSourceImport.value());
            log.info("========数据源切换至：{}", dataSourceImport.value());
            return pjp.proceed();
        } finally {
            if (clear) {
                RedisSourceContextHolder.clear();
            }

        }
    }
    private Method getMethod(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        return signature.getMethod();
    }


}