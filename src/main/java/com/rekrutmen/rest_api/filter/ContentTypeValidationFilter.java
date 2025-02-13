package com.rekrutmen.rest_api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ContentTypeValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip validation for preflight OPTIONS requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Skip validation for the /image/{gambar} endpoint
        String requestUri = request.getRequestURI();
        if (requestUri.matches("/api/artikel/image/.*")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Validate Content-Type
        if (!MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getHeader("Content-Type"))) {
            logger.warn("Invalid Content-Type. Expected application/json");

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\":\"Invalid Content-Type. Expected application/json\"}");
            return;
        }

        // Validate Accept header
        if (!MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getHeader("Accept"))) {
            logger.warn("Invalid Accept header. Expected application/json");

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\":\"Invalid Accept header. Expected application/json\"}");
            return;
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}
