package com.pdfa.msl.webaj.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pdfier.security.AccessControlService;

public class LisenceAccessControlFilter extends OncePerRequestFilter {
	@Autowired
	AccessControlService accessControlService;

	public LisenceAccessControlFilter() {
	}

	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String lisence = request.getParameter("lisence");
		if (lisence != null) {
			if (accessControlService.validLisence(lisence)) {
				chain.doFilter(request, response);
			} else {
				response.sendError(402, "No valid lisence");
			}
		} else {
			response.sendError(400, "lisence not found in the request");
		}
	}
}