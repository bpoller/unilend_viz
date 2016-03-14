package io.nowave.api.security;

import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class JWTTokenFilter extends GenericFilterBean {

    private final Logger logger = getLogger(getClass());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        Optional<Cookie[]> cookies = Optional.ofNullable(servletRequest.getCookies());
        Optional<Cookie> jwtCookie = asList(cookies.orElse(new Cookie[0])).stream()
                .filter((cookie -> cookie.getName().equals("jwt"))).findFirst();

        jwtCookie.ifPresent((this::addToSecurityContext));

        chain.doFilter(request, response);
    }

    private void addToSecurityContext(Cookie cookie) {
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(cookie.getValue()));
    }
}


