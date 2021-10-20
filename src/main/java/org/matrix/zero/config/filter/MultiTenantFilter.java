package org.matrix.zero.config.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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