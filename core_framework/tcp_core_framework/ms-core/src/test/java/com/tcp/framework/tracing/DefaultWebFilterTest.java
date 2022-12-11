package com.tcp.framework.tracing;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;

class DefaultWebFilterTest {

	@InjectMocks
	private DefaultWebFilter defaultWebFilter = new DefaultWebFilter();

	@Test
	void testPreHandle() throws Exception {
		WebRequest request = mock(WebRequest.class);
		defaultWebFilter.preHandle(request);
		assertNotNull(request);
	}

	@Test
	void testPostHandle() throws Exception {
		WebRequest request = mock(WebRequest.class);
		ModelMap modelMap = mock(ModelMap.class);

		defaultWebFilter.postHandle(request, modelMap);
		assertNotNull(request);
		assertNotNull(modelMap);
	}

	@Test
	void testAfterCompletion() throws Exception {
		WebRequest request = mock(WebRequest.class);

		defaultWebFilter.afterCompletion(request, new Exception());
		assertNotNull(request);
	}
}
