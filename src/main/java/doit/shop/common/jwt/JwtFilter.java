package doit.shop.common.jwt;

import doit.shop.controller.user.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.equals("/api/users/login") || uri.equals("/api/users/signup") || uri.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtProvider.resolveToken(request);
        String userId = jwtProvider.getUserId(token);
        String refreshToken = jwtProvider.resolveRefreshToken(request);
        System.out.println("tt : "+refreshToken);
        System.out.println("? : "+jwtProvider.isExpired(tokenRepository.findByLoginId(userId).getRefreshToken()));
        if (jwtProvider.isValidToken(token, userId)){
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (!jwtProvider.isExpired(tokenRepository.findByLoginId(userId).getRefreshToken())) {
            System.out.println("this?");
            Authentication authentication = jwtProvider.getAuthentication(refreshToken);
            String accessToken = jwtProvider.generateAccessToken(authentication);
            response.setHeader("Authorization", "Bearer " + accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}