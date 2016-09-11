package com.pdfier.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pdfier.service.security.AccessControlService;

/**
 * Servlet Filter implementation class DomainAccessControlFilter
 */
public class LisenceAccessControlFilter extends OncePerRequestFilter {
	
	@Autowired
	AccessControlService accessControlService;

    /**
     * Default constructor. 
     */
    public LisenceAccessControlFilter() {
    }


	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String lisence = request.getParameter("lisence");
		if(lisence != null){
			if(accessControlService.validLisence(lisence)){
				// pass the request along the filter chain
				chain.doFilter(request, response);
			}else{
				response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, "No valid lisence");
			}
		}else{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "lisence not found in the request");
		}
		
	}
}
