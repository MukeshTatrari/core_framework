package com.tcp.framework.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 
 * configuration for web Filter
 *
 */
@Configuration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class WebFilterConfiguration implements WebMvcConfigurer {

	@Autowired
	DefaultWebFilter filter;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addWebRequestInterceptor(filter).excludePathPatterns("/error");
	}

}
