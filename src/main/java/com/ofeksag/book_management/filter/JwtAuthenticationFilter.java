package com.ofeksag.book_management.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofeksag.book_management.dto.ErrorResponseDTO;
import com.ofeksag.book_management.exception.JwtDeserializationException;
import com.ofeksag.book_management.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui.html") || path.startsWith("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = getJWTFromRequest(request);
        if (!StringUtils.hasText(jwt)) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing Token", "Authorization header with Bearer token is required");
            return;
        }

        try {
            String username = jwtUtil.extractUsername(jwt);
            if (username != null && jwtUtil.validateToken(jwt, username)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtDeserializationException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid JWT token", e.getMessage());
            return;
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Error processing JWT token", e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String error, String message)
            throws IOException {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(error, message);
        response.setStatus(status);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), errorResponseDTO);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}