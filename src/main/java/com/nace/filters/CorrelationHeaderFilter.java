package com.nace.filters;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter to set the correlation id (for tracing purpose) in the inbound
 * request.
 * 
 * @author Deepak Bhalla
 *
 */
public class CorrelationHeaderFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(CorrelationHeaderFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String currentCorrId = httpServletRequest.getHeader(RequestCorrelation.CORRELATION_ID_HEADER);

        if (currentCorrId == null) {
            currentCorrId = UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY);
            LOG.info("New Correlation Id Generated: " + currentCorrId);
        } else {
            LOG.info("Found Existing Correlation Id: " + currentCorrId);
        }

        RequestCorrelation.setId(currentCorrId);
        filterChain.doFilter(httpServletRequest, response);
    }
}
