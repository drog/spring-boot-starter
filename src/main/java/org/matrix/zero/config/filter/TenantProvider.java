package org.matrix.zero.config.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class TenantProvider {

    public String getTenant() {
        String tenant = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes)  RequestContextHolder.getRequestAttributes()).getRequest();
            tenant = request != null ? request.getHeader("x-country") : null;
        }
        return tenant;
    }
}
