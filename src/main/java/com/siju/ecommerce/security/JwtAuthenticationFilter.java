package com.siju.ecommerce.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.siju.ecommerce.user.CustomUserDetailsService;
import com.siju.ecommerce.user.User;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final CustomUserDetailsService userDetailsService;
        private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain)
                        throws IOException, ServletException {
                // Implement JWT authentication logic here
                String authHeader = request.getHeader("Authorization");
                logger.debug("Authorization header = {}", authHeader);
                if (authHeader == null
                                || !authHeader.startsWith("Bearer ")) {

                        filterChain.doFilter(request, response);
                        return;
                }

                String jwt = authHeader.substring(7);

                String username = null;
                try {
                        username = jwtService.extractUsername(jwt);
                } catch (ExpiredJwtException ex) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");

                        response.getWriter().write("""
                                        {
                                            "status": 401,
                                            "error": "Unauthorized",
                                            "message": "JWT token has expired"
                                        }
                                        """);

                        return;
                }

                if (username != null
                                && SecurityContextHolder
                                                .getContext()
                                                .getAuthentication() == null) {

                        UserDetails userDetails = userDetailsService
                                        .loadUserByUsername(
                                                        username);

                        if (jwtService.isTokenValid(
                                        jwt,
                                        (User) userDetails)) {
                                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                null,
                                                userDetails.getAuthorities());

                                authToken.setDetails(
                                                new WebAuthenticationDetailsSource()
                                                                .buildDetails(request));

                                SecurityContextHolder
                                                .getContext()
                                                .setAuthentication(authToken);
                        }
                }

                filterChain.doFilter(request, response);
        }
}
