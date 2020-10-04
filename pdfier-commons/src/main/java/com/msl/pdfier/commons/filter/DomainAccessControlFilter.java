package com.msl.pdfier.commons.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.msl.pdfier.commons.security.AccessControlService;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
public class DomainAccessControlFilter extends OncePerRequestFilter {

	@Autowired
	AccessControlService accessControlService;

	public DomainAccessControlFilter() {
	}

	/**
	 * Check the referrer header If not null validate against valid domains If valid
	 * domain then chain the request Else traceIP and validate the license If not
	 * valid return traceIP and return error Else traceIP and return error
	 * 
	 * @param request  the http request
	 * @param response the http response
	 * @param chain    the chain
	 * @throws IOException      io error
	 * @throws ServletException servlet error
	 */
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String referrer = request.getHeader("referer");
		String license = request.getParameter("license");
		if (referrer != null) {
			if (accessControlService.validReferrer(referrer)) {
				chain.doFilter(request, response);
			} else {
				traceIP(request, "referer", referrer);
				if (accessControlService.validLicense(license)) {
					chain.doFilter(request, response);
				} else {
					traceIP(request, "license", license);
					response.sendError(402,
							"No valid referrer. After you acquire the license, you need to set the domain(s) of your website(s) in our members area.");
				}
			}
		} else {
			traceIP(request, "referer", "");
			if (accessControlService.validLicense(license)) {
				chain.doFilter(request, response);
			} else {
				traceIP(request, "license", license);
				response.sendError(402,
						"No referrer found in the request. After you acquire the license, you need to set the domain(s) of your website(s) in our members area.");
			}
		}
	}

	private void traceIP(HttpServletRequest request, String requestParamName, String requestParamValue) {
		String ip = request.getRemoteAddr();
		String xForwardedFor = request.getHeader("x-forwarded-for");
		log.info("No valid " + requestParamName + ":" + requestParamValue + ", remote ip:" + ip + ", x-forwarded-for:"
				+ xForwardedFor);
	}
}
