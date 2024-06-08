package org.etf.unibl.SecureForum.security.xss_filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.BadRequestException;
import org.etf.unibl.SecureForum.exceptions.UnauthorizedException;
import org.etf.unibl.SecureForum.exceptions.servlet_exceptions.XSSAttackException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.LogRecord;

@Component
public class XSSFilter extends OncePerRequestFilter {


    @Override
    public void doFilterInternal(@NotNull HttpServletRequest request,
                                 @NotNull HttpServletResponse response,
                                 @NotNull FilterChain filterChain)
            throws IOException, ServletException {


        // Check parameters
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            for (String paramValue : paramValues) {
                if (containsScriptTag(paramValue)) {
                    throw new XSSAttackException("Potential XSS attack detected in parameter: " + paramName);
                }
            }
        }

        // Check headers
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if (containsScriptTag(headerValue)) {
                throw new XSSAttackException("Potential XSS attack detected in header: " + headerName);
            }

        }

        XSSRequestWrapper sanitizedRequest = new XSSRequestWrapper(request);
        
        filterChain.doFilter(sanitizedRequest,response);
    }

    private boolean containsScriptTag(String value) {
        return value != null && (value.toLowerCase().contains("<script>") || value.toLowerCase().contains("</script>"));
    }

}
