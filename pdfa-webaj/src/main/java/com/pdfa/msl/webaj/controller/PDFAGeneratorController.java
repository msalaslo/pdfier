package com.pdfa.msl.webaj.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.msl.pdfa.pdf.exception.PdfUAGenerationException;
import com.msl.pdfa.pdf.gen.HTMLToPDFConverter;

@Controller
public class PDFAGeneratorController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/pdfafromurl", produces = "application/pdf")
	public void pdfaFromUrl(@RequestParam("url") String url, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("antes");
		try {
			if (url != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int size = generateAndWritePDF(new URL(url), out);			
				configureResponse(response, size);
				out.writeTo(response.getOutputStream());
				response.getOutputStream().flush();
			} else {
				logger.warn("url param can not be null");
			}
		} catch (Exception e) {
			logger.error("Error generating PDF", e);
		}
	}

	@RequestMapping(value = "/pdfafull", method = RequestMethod.POST, produces = "application/pdf")
	@ResponseBody
	public HttpServletResponse pdfaHtmlWithParams(@RequestParam("html") String html, @RequestParam("language") String language, @RequestParam("title") String title, HttpServletRequest request, HttpServletResponse response) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int size = generateAndWritePDF(new URL(request.getRequestURL().toString()), html, out);			
			configureResponse(response, size);
			out.writeTo(response.getOutputStream());
			response.getOutputStream().flush();
		} catch (Exception e) {
			logger.error("Error generating PDF /pdfafull", e);
		}
		return response;
	}

	private int generateAndWritePDF(URL sourceUrl, OutputStream out) throws IOException, PdfUAGenerationException {
		if (sourceUrl != null && !"".equals(sourceUrl)) {
			System.out.println("hola");
			return HTMLToPDFConverter.htmlToPDF(sourceUrl, out);
		}else{
			return 0;
		}
	}

	private int generateAndWritePDF(URL requestUrl, String html, OutputStream out) throws IOException, PdfUAGenerationException {
		if (!"".equals(html)) {
			return HTMLToPDFConverter.htmlToPDF(requestUrl, html, out);
		}else{
			return 0;
		}
	}

	private void configureResponse(HttpServletResponse response, int contentLenght) throws IOException {
 		response.setContentType("application/pdf");
//		response.addHeader("Content-Type","application/force-download");
		response.addHeader("Content-Disposition", "attachment; filename=pdfaGenerated.pdf");
		response.addHeader("Accept-ranges", "none");
		response.setContentLength(contentLenght);
	}

	@RequestMapping(value = "/test")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String now = (new Date()).toString();
		logger.info("Returning hello view with " + now);
		return new ModelAndView("test", "now", now);
	}
}
