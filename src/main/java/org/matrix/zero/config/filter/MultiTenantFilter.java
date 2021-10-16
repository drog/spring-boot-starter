package org.matrix.zero.config.filter;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class MultiTenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        if (!req.getMethod().equalsIgnoreCase("OPTIONS")) {
            if (req.getHeader("x-country") != null) {
                log.info("x-country: {}", req.getHeader("x-country"));
            } else {
                throw new ServletException("header x-country required");
            }
        }
        chain.doFilter(request, response);
    }
}