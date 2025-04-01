package com.ofeksag.book_management.filter;

import com.ofeksag.book_management.exception.RequestBodyNotAllowedException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ValidateWithoutBodyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            try {
                validateWithoutBodyForGetDelete(httpRequest);
                chain.doFilter(request, response);
            } catch (RequestBodyNotAllowedException ex) {
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.getWriter().write(ex.getMessage());
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void validateWithoutBodyForGetDelete(HttpServletRequest request) {
        String method = request.getMethod();
        if (("GET".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))
                && request.getContentLength() > 0) {
            throw new RequestBodyNotAllowedException("Body is not allowed for " + method + " requests.");
        }
    }
}