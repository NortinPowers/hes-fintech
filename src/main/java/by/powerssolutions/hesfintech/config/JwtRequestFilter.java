package by.powerssolutions.hesfintech.config;

import static by.powerssolutions.hesfintech.utils.ResponseUtils.EXPIRED_JWT_EXCEPTION_MESSAGE;
import static by.powerssolutions.hesfintech.utils.ResponseUtils.MALFORMED_JWT_EXCEPTION_MESSAGE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import by.powerssolutions.hesfintech.handler.CustomAccessDeniedHandler;
import by.powerssolutions.hesfintech.token.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            try {
                String username = jwtTokenManager.getUsername(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            jwtTokenManager.getUserRoles(jwt).stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    request.setAttribute(SecurityContextHolderFilter.class.getName() + ".APPLIED", Boolean.TRUE);
                }
            } catch (MalformedJwtException exception) {
                createExceptionResponseAndLog(request, response, MALFORMED_JWT_EXCEPTION_MESSAGE, exception);
                return;
            } catch (ExpiredJwtException exception) {
                createExceptionResponseAndLog(request, response, EXPIRED_JWT_EXCEPTION_MESSAGE, exception);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void createExceptionResponseAndLog(HttpServletRequest request,
                                               HttpServletResponse response,
                                               String message,
                                               Exception exception) throws IOException, ServletException {
        log.debug(exception.getMessage());
        customAccessDeniedHandler.handle(request, response, new AccessDeniedException(message));
    }
}
