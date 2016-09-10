package com.pdfier.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class DomainAccessControlFilter
 */
public class DomainAccessControlFilter implements Filter {

    /**
     * Default constructor. 
     */
    public DomainAccessControlFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		String referrer = req.getHeader("referer");
		if(referrer != null){
			if(validReferrer(referrer)){
				// pass the request along the filter chain
				chain.doFilter(request, response);
			}
		}
		res.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
	}
	
	private boolean validReferrer(String referrer){
		if(referrer != null && (referrer.startsWith("http://pdfier.com") || referrer.startsWith("http://www.pdfier.com")|| referrer.startsWith("http://localhost/"))){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}

}
