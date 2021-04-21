package com.ebizcipta.ajo.api.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        if (request.getHeader("Origin") != null) {
//            response.setHeader("Access-Control-Allow-Origin", "https://ajo.bgonline.uob.co.id");
//            response.setHeader("Access-Control-Allow-Origin", "https://u-bgonline.uob.co.id");
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, X-Requested-With, Content-Type, Accept");
            response.setHeader("Access-Control-Max-Age", "1800"); // 30 min
            response.setHeader("Access-Control-Expose-Headers", "Total-Count, Total-Pages, Error-Message");
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(req, res);
        }

    }

    @Override
    public void destroy() {

    }
}
