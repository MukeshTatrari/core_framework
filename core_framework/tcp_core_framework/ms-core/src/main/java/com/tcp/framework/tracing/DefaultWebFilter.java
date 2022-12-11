package com.tcp.framework.tracing;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Default web filter for intercepting all incoming request and appending the
 * traceID and sessionId in the loggers.
 *
 */
@Component
@Slf4j
class DefaultWebFilter implements WebRequestInterceptor {

	private static final String TCP_TRACE_SESSION_ID = "tcp-trace-session-id";
	private static final String TCP_TRACE_REQUEST_ID = "tcp-trace-request-id";
	private static final String LEGACY_EXPORTABLE_NAME = "X-Span-Export";
	private static final String LEGACY_TRACE_ID_NAME = "X-B3-TraceId";
	private static final String LEGACY_SPAN_ID_NAME = "X-B3-SpanId";

	/**
	 * get tracing session Id and request id from headers, and adding them in the
	 * logger MDC
	 */
	@Override
	public void preHandle(WebRequest request) throws Exception {

		log.debug("inside DefaultWebFilter prehandle");

		String requestedTraceId = !ObjectUtils.isEmpty(request.getHeader(TCP_TRACE_REQUEST_ID))
				? request.getHeader(TCP_TRACE_REQUEST_ID)
				: UUID.randomUUID().toString();

		String requestedSessionId = !ObjectUtils.isEmpty(request.getHeader(TCP_TRACE_SESSION_ID))
				? request.getHeader(TCP_TRACE_SESSION_ID)
				: UUID.randomUUID().toString();

		// using combination of requestedTraceId and requestedSessionId for logger
		// traceId
		StringBuilder traceId = new StringBuilder().append(requestedTraceId).append(requestedSessionId);
		String sessionId = UUID.randomUUID().toString();

		// make a context map of traceId & session ID to pass to MDC context.
		final Map<String, String> contextMap = new HashMap<>();
		contextMap.put(LEGACY_TRACE_ID_NAME, traceId.toString());
		contextMap.put(LEGACY_SPAN_ID_NAME, sessionId);
		contextMap.put(LEGACY_EXPORTABLE_NAME, "true");

		MDC.setContextMap(contextMap);

	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {

		log.debug("inside DefaultWebFilter postHandle");

	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {

		log.debug("inside DefaultWebFilter afterCompletion");
	}

}