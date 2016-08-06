package com.pdfa.msl.webaj.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.msl.pdfa.pdf.exception.PdfUAGenerationException;
import com.msl.pdfa.pdf.gen.HTMLToPDFConverter;

@RestController
public class PDFAGeneratorController {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "/pdfafromurl", method = RequestMethod.POST, produces = "application/pdf")
	@ResponseBody
	public HttpServletResponse pdfaFromUrl(@RequestParam("url") String url,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			if(url != null){
				genPDF(new URL(url), response);
			}else{
				logger.warn("url param can not be null");
			}
		}catch(Exception e){
			logger.error("Error generating PDF", e);
		}
		return response;
	}

	@RequestMapping(value = "/pdfa", method = RequestMethod.POST, produces = "application/pdf")
	@ResponseBody
	public HttpServletResponse pdfaHtml(@RequestParam("html") String html,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			genPDF(new URL(request.getRequestURL().toString()), html, response);
			//response.setContentLength(arg0);
		}catch(Exception e){
			logger.error("Error generating PDF", e);
		}
		return response;
	}
	
	@RequestMapping(value = "/pdfafull", method = RequestMethod.POST, produces = "application/pdf")
	@ResponseBody
	public HttpServletResponse pdfaHtmlWithParams(@RequestParam("html") String html, 
			@RequestParam("language") String language,
			@RequestParam("title") String title, 
			HttpServletRequest request, HttpServletResponse response) {
		try{
			genPDF(new URL(request.getRequestURL().toString()), html, response);
			//response.setContentLength(arg0);
		}catch(Exception e){
			logger.error("Error generating PDF", e);
		}
		return response;
	}
	
	protected void genPDF(URL sourceUrl, HttpServletResponse response) throws IOException, PdfUAGenerationException{
		if(sourceUrl != null && !"".equals(sourceUrl)){
			OutputStream out = response.getOutputStream();
			HTMLToPDFConverter.htmlToPDF(sourceUrl, out);
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=pdfaGenerated.pdf");
			response.addHeader("Accept-ranges", "none");
		}
	}
	
	protected void genPDF(URL requestUrl, String html, HttpServletResponse response) throws IOException, PdfUAGenerationException{
		if(!"".equals(html)){
			OutputStream out = response.getOutputStream();
			HTMLToPDFConverter.htmlToPDF(requestUrl, html, out);
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=pdfaGenerated.pdf");
			response.addHeader("Accept-ranges", "none");
		}
	}
	
    @RequestMapping(value="/test")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String now = (new Date()).toString();
        logger.info("Returning hello view with " + now);
        return new ModelAndView("test", "now", now);
    }
}
