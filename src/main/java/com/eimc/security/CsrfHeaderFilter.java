package com.eimc.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfHeaderFilter extends OncePerRequestFilter {

    /**
     *      Custom filter to facilitate
     *      CSRF protection.
     *
     *      doFilterInternal triggers the
     *      generation of the CsrfToken and explicitly
     *      includes it in the response headers
     *      to ensure synchronization between
     *      the server and the frontend client.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /// Retrieve the CsrfToken from the request attribute.
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (null != csrfToken){

            /// Trigger the CsrfToken to be included in the Set-Cookie header.
            String tokenValue = csrfToken.getToken();

            /// Manually attach the CsrfToken in the response header for frontend client
            response.setHeader(csrfToken.getHeaderName(), tokenValue);
        }

        /// Pass the request/response to the next filter
        filterChain.doFilter(request, response);
    }

}
