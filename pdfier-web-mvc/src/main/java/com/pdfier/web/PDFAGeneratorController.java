package com.pdfier.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.msl.pdfa.pdf.exception.PdfUAGenerationException;
import com.msl.pdfa.pdf.gen.HTMLToPDFConverter;

@Controller
public class PDFAGeneratorController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/pdfafromurl", produces = "application/pdf")
	public void pdfaFromUrl(@RequestParam(value = "url", required=true) String url, @RequestParam(value = "fileName", required=false) String fileName, @RequestParam(value = "inline", required=false) boolean inline, HttpServletRequest request, HttpServletResponse response) {	
		try {
			if (url != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int size = generateAndWritePDF(new URL(url), out);			
				writeToResponse(response, out, size, fileName, inline);
			} else {
				logger.warn("url param can not be null");
			}
		} catch (Exception e) {
			logger.error("Error generating PDF", e);
		}
	}
	
	@RequestMapping(value = "/saveaspdfua", produces = "application/pdf")
	public void saveAsPdfUA(@RequestParam(value = "url", required=true) String url, @RequestParam(value = "fileName", required=false) String fileName, @RequestParam(value = "inline", required=false) boolean inline, HttpServletRequest request, HttpServletResponse response) {	
		try {
			if (url != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int size = generateAndWritePDF(new URL(url), out);			
				writeToResponse(response, out, size, fileName, inline);
			} else {
				logger.warn("url param can not be null");
			}
		} catch (Exception e) {
			logger.error("Error generating PDF", e);
		}
	}
	
	@RequestMapping(value = "/api", produces = "application/pdf")
	public void apiPdfFromUrl(@RequestParam(value = "key", required=true) String key, @RequestParam(value = "url", required=true) String url, @RequestParam(value = "fileName", required=false) String fileName, @RequestParam(value = "inline", required=false) boolean inline, HttpServletRequest request, HttpServletResponse response) {	
		try {
			if (url != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int size = generateAndWritePDF(new URL(url), out);			
				writeToResponse(response, out, size, fileName, inline );
			} else {
				logger.warn("url param can not be null");
			}
		} catch (Exception e) {
			logger.error("Error generating PDF", e);
		}
	}

	@RequestMapping(value = "/pdfafull", method = RequestMethod.POST, produces = "application/pdf")
	@ResponseBody
	public HttpServletResponse pdfaHtmlWithParams(@RequestParam(value = "html", required=true) String html, @RequestParam(value = "fileName", required=false) String fileName, @RequestParam(value = "inline", required=false) boolean inline, @RequestParam("language") String language, @RequestParam("title") String title, HttpServletRequest request, HttpServletResponse response) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int size = generateAndWritePDF(new URL(request.getRequestURL().toString()), html, out);	
			writeToResponse(response, out, size, fileName, inline);
		} catch (Exception e) {
			logger.error("Error generating PDF /pdfafull", e);
		}
		return response;
	}
	
	private void writeToResponse(HttpServletResponse response, ByteArrayOutputStream content, int size, String fileName, boolean inline) throws IOException{
		configureResponse(response, size, fileName, inline);
		content.writeTo(response.getOutputStream());
		response.getOutputStream().flush();
	}

	private int generateAndWritePDF(URL sourceUrl, OutputStream out) throws IOException, PdfUAGenerationException {
		return HTMLToPDFConverter.htmlToPDF(sourceUrl, out);
	}

	private int generateAndWritePDF(URL requestUrl, String html, OutputStream out) throws IOException, PdfUAGenerationException {
		return HTMLToPDFConverter.htmlToPDF(requestUrl, html, out);
	}

	private void configureResponse(HttpServletResponse response, int contentLenght, String fileName, boolean inline) throws IOException {
 		response.setContentType("application/pdf");
//		response.addHeader("Content-Type","application/force-download");
 		String downloadFileName = "PDFier.pdf";
 		if(fileName != null){
 			downloadFileName = fileName;
 		}
 		if(inline){
 			response.addHeader("Content-Disposition", "inline; filename=" + downloadFileName);
 		}else{
 			response.addHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
 		}
		response.addHeader("Accept-ranges", "none");
		response.setContentLength(contentLenght);
	}
}
