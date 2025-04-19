package com.example.demo.security;

import com.example.demo.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String path = request.getServletPath();

        // ✅ Skip JWT check for auth-related paths
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.validateToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var userDetails = userService.loadUserByUsername(username);
                    var auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e) {
                System.out.println("❌ Invalid token: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
