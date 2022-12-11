package com.tcp.framework.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

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

		Object response = joinPoint.proceed();

		long timeTaken = System.currentTimeMillis() - startTime;
		log.info("Total Time Taken by {} is {} milliseconds", joinPoint, timeTaken);
		return response;
	}

}
