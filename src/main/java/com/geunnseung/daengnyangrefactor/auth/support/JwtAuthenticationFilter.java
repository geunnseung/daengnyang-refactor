package com.geunnseung.daengnyangrefactor.auth.support;

import com.geunnseung.daengnyangrefactor.auth.token.AccessTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auth/signup",
            "/api/auth/login",
            "/api/auth/reissue",
            "/api/auth/logout"
    );

    private final AccessTokenProvider accessTokenProvider;

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return EXCLUDED_PATHS.contains(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());
        Long userId = accessTokenProvider.getUserId(accessToken);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser,
                null,
                List.of()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
