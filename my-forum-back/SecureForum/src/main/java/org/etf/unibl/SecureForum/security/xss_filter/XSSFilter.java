package org.etf.unibl.SecureForum.security.xss_filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.BadRequestException;
import org.etf.unibl.SecureForum.exceptions.UnauthorizedException;
import org.etf.unibl.SecureForum.exceptions.servlet_exceptions.XSSAttackException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.logging.LogRecord;

@Component
public class XSSFilter extends OncePerRequestFilter {


    @Override
    public void doFilterInternal(@NotNull HttpServletRequest request,
                                 @NotNull HttpServletResponse response,
                                 @NotNull FilterChain filterChain)
            throws IOException, ServletException {


       System.out.println(request.getParameter("code"));
        // Check parameters
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            for (String paramValue : paramValues) {
                if (containsScriptTag(paramValue)) { //if the parameters have a script tag
                    HttpSession session = request.getSession(false); //LOGOUT FROM SESSION IF HE IS SENDING MALICIOUS CONTENT
                    if (session != null) {
                        session.invalidate();
                    }
                    throw new XSSAttackException("Potential XSS attack detected in parameter: " + paramName);
                }
            }
        }

        // Check headers
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if (containsScriptTag(headerValue)) { //If the header has a malicious script tag
                HttpSession session = request.getSession(false); //LOGOUT THIS USER FROM SESSION IF HE DID AN ATTACK
                if (session != null) {
                    session.invalidate();
                }
                throw new XSSAttackException("Potential XSS attack detected in header: " + headerName);
            }
        }

     //   String requestBody = extractRequestBody(request);
     //   if (containsScriptTag(requestBody)) {
     //       throw new XSSAttackException("Potential XSS attack detected in request body");
     //   }

        XSSRequestWrapper sanitizedRequest = new XSSRequestWrapper(request);


        filterChain.doFilter(sanitizedRequest,response);
    }

    private boolean containsScriptTag(String value) {
        return value != null && (value.toLowerCase().contains("<script>") || value.toLowerCase().contains("</script>"));
    }

    private String extractRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        return stringBuilder.toString();
    }

}
