package com.tcp.framework.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class PerformanceExecutionAspectTest {

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@InjectMocks
	PerformanceExecutionAspect performaceExecutionAspect;

	@Test
	public void logAroundTest() throws Throwable {

		ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
		performaceExecutionAspect.around(joinPoint);
	}
}
