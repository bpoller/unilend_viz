package io.nowave.api.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class IPGeolocationFilter implements Filter {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing IPGeolocationFilter...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            String countryCode = httpServletRequest.getHeader("CF-IPCountry");

            if (countryCode != null) {
                logger.debug("The client country is " + countryCode);

                switch (countryCode.toLowerCase()) {
                    case "fr":
                        // TODO
                        break;
                    case "uk":
                        break;
                        // TODO
                    default:
                        // TODO
                }
            }

        }

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
    }
}
