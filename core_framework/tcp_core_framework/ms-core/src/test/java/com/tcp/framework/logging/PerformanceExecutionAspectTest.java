package com.tcp.framework.logging;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tcp.framework.annotations.PerformanceLogger;

class PerformanceExecutionAspectTest {

	@InjectMocks
	PerformanceExecutionAspect performaceExecutionAspect;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void logAroundTest() throws Throwable {

		ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
		MethodSignature signature = mock(MethodSignature.class);
		when(joinPoint.getSignature()).thenReturn(signature);
		when(signature.getMethod()).thenReturn(myMethod());
		when(joinPoint.proceed()).thenReturn(new String("SuccessFul"));

		Object response = performaceExecutionAspect.around(joinPoint);

		assertEquals(response, new String("SuccessFul"));
	}

	@Test
	void logAroundWithoutPerformaceAnotationTest() throws Throwable {

		ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
		MethodSignature signature = mock(MethodSignature.class);
		when(joinPoint.getSignature()).thenReturn(signature);
		when(signature.getMethod()).thenReturn(testlogMethod());
		when(joinPoint.proceed()).thenReturn(new String("SuccessFul"));

		Object response = performaceExecutionAspect.around(joinPoint);

		assertEquals(response, new String("SuccessFul"));
	}

	public Method myMethod() throws NoSuchMethodException, SecurityException {
		return getClass().getDeclaredMethod("someMethod");
	}

	public Method testlogMethod() throws NoSuchMethodException, SecurityException {
		return getClass().getDeclaredMethod("testMethod");
	}

	@PerformanceLogger("hello")
	public String someMethod() {
		return "hello";
	}

	@PerformanceLogger()
	public String testMethod() {
		return "hello";
	}
}