package com.pdfier.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pdfier.service.security.AccessControlService;

/**
 * Servlet Filter implementation class DomainAccessControlFilter
 */
public class DomainAccessControlFilter extends OncePerRequestFilter {
	
	@Autowired
	AccessControlService accessControlService;

    /**
     * Default constructor. 
     */
    public DomainAccessControlFilter() {
    }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String referrer = request.getHeader("referer");
		if(referrer != null){
			if(accessControlService.validReferrer(referrer)){
				// pass the request along the filter chain
				chain.doFilter(request, response);
			}else{
				response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, "No valid referrer. After you acquire the license, you need to set the domain(s) of your website(s) in our members area.");
			}
		}else{
			response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, "No referrer found in the request. After you acquire the license, you need to set the domain(s) of your website(s) in our members area.");
		}
	}
}
