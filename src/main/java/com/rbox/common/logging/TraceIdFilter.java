package com.rbox.common.logging;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter that assigns a trace identifier for each incoming request and
 * logs basic request information. The identifier is added to the MDC so
 * that it can be included in subsequent log messages, allowing requests
 * to be traced even under heavy load.
 */
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_KEY = "traceId";

    private static final Logger log = LoggerFactory.getLogger(TraceIdFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = obtainTraceId(request);
        MDC.put(TRACE_ID_KEY, traceId);
        log.info("[traceId={}] Incoming request {} {} from {}", traceId,
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }

    private String obtainTraceId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? session.getId() : UUID.randomUUID().toString();
    }
}
