package com.msl.pdfier.commons.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.msl.pdfier.commons.security.AccessControlService;

public class DomainAccessControlFilter extends OncePerRequestFilter {
	@Autowired
	AccessControlService accessControlService;

	public DomainAccessControlFilter() {
	}

	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String referrer = request.getHeader("referer");
		if (referrer != null) {
			if (accessControlService.validReferrer(referrer)) {
				chain.doFilter(request, response);
			} else {
				response.sendError(402,
						"No valid referrer. After you acquire the license, you need to set the domain(s) of your website(s) in our members area.");
			}
		} else {
			response.sendError(402,
					"No referrer found in the request. After you acquire the license, you need to set the domain(s) of your website(s) in our members area.");
		}
	}
}
