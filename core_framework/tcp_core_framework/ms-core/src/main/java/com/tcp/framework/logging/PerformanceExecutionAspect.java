package com.tcp.framework.logging;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import com.tcp.framework.annotations.PerformanceLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * Aspect for the performance logging based on the conditional property
 */
@Aspect
@Configuration
@Slf4j
@ConditionalOnExpression("${performance.service.enabled:false}")
public class PerformanceExecutionAspect {

	/**
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 * 
	 *                   Log the total time taken to execute the request by a
	 *                   method.
	 */
	@Around("@annotation(com.tcp.framework.annotations.PerformanceLogger)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		PerformanceLogger myAnnotation = method.getAnnotation(PerformanceLogger.class);
		String value = myAnnotation.value();
		if (ObjectUtils.isEmpty(value)) {
			String url = !ObjectUtils.isEmpty(joinPoint.getArgs()) ? String.valueOf(joinPoint.getArgs()[0]) : "";

			value = method.getDeclaringClass().getName() + " || " + method.getName() + " || " + url;
		}
		Object response = joinPoint.proceed();
		long timeTaken = System.currentTimeMillis() - startTime;
		log.info("Total Time Taken by :: {} is {} milliseconds", value, timeTaken);
		return response;
	}

}
