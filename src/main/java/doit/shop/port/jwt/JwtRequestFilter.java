package doit.shop.port.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import doit.shop.exception.ErrorInfo;
import doit.shop.port.jwt.dto.BasicResDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtPort jwtPort;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtRequestFilter(JwtPort jwtPort, @Lazy CustomUserDetailsService customUserDetailsService) {
        this.jwtPort = jwtPort;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI().replace(contextPath, "");

        if (isAuthenticatedEndpoint(path)) {
            try {
                String authorizationHeader = request.getHeader("Authorization");
                String token = null;

                // Authorization 헤더에서 Bearer 토큰 추출
                if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
                    token = authorizationHeader.substring(7).trim();
                }

                // 토큰이 존재하고, 인증이 설정되지 않았으며, 토큰이 만료되지 않았을 경우
                if (token != null && SecurityContextHolder.getContext().getAuthentication() == null && !jwtPort.isTokenExpired(token)) {
                    String username = jwtPort.extractEmail(token);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    // 토큰 유효성 검증
                    if (jwtPort.isValidateToken(token, username)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // SecurityContext에 인증 정보 설정
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // 예외 발생 시 JSON 형식의 에러 응답 반환
                response.setStatus(ErrorInfo.SECURITY_ERROR.getStatus().value());
                BasicResDto responseDto = new BasicResDto("Invalid JWT token");
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(responseDto);

                response.setContentType("application/json");
                response.getWriter().write(jsonResponse);
                return; // 예외 발생 시 체인 실행 중단
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isAuthenticatedEndpoint(String path) {
        return Arrays.stream(APIPaths.AUTHENTICATED_ENDPOINTS).anyMatch(path::matches);
    }
}
