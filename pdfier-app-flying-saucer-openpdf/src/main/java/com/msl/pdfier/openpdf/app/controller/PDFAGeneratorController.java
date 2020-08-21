package com.msl.pdfier.openpdf.app.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.msl.pdfier.openpdf.app.service.pdf.PDFGenService;
import com.msl.pdfier.openpdf.gen.exceptions.PdfUAGenerationException;

@Controller
public class PDFAGeneratorController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PDFGenService pdfGenService;

	@RequestMapping(value = { "/pdfuafromurl" }, produces = { "application/pdf" })
	public void pdfUAFromUrl(@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "inline", required = false) boolean inline, HttpServletRequest request,
			HttpServletResponse response) {
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

	@RequestMapping(value = { "/saveaspdfua" }, produces = { "application/pdf" })
	public void saveAsPdfUA(@RequestParam(value = "url", required = true) String url,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "inline", required = false) boolean inline, HttpServletRequest request,
			HttpServletResponse response) {
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

	@RequestMapping(value = { "/pdfuafromhtml" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST }, produces = { "application/pdf" })
	@ResponseBody
	public HttpServletResponse pdfUAFromHtml(@RequestParam(value = "html", required = true) String html,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "inline", required = false) boolean inline,
			@RequestParam(value = "language", required = true) String language,
			@RequestParam(value = "title", required = true) String title, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int size = generateAndWritePDF(new URL(request.getRequestURL().toString()), html, out);
			writeToResponse(response, out, size, fileName, inline);
		} catch (Exception e) {
			logger.error("Error generating PDF /pdfuafromhtml", e);
		}
		return response;
	}

	private void writeToResponse(HttpServletResponse response, ByteArrayOutputStream content, int size, String fileName,
			boolean inline) throws IOException {
		configureResponse(response, size, fileName, inline);
		content.writeTo(response.getOutputStream());
		response.getOutputStream().flush();
	}

	private int generateAndWritePDF(URL sourceUrl, OutputStream out) throws IOException, PdfUAGenerationException {
		return pdfGenService.generateAndWritePDF(sourceUrl, out);
	}

	private int generateAndWritePDF(URL sourceUrl, String html, OutputStream out)
			throws IOException, PdfUAGenerationException {
		return pdfGenService.generateAndWritePDF(sourceUrl, html, out);
	}

	private void configureResponse(HttpServletResponse response, int contentLenght, String fileName, boolean inline)
			throws IOException {
		response.setContentType("application/pdf");
		String downloadFileName = "PDFier.pdf";
		if (fileName != null) {
			downloadFileName = fileName;
		}
		if (inline) {
			response.addHeader("Content-Disposition", "inline; filename=" + downloadFileName);
		} else {
			response.addHeader("Content-Type", "application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
		}
		response.addHeader("Accept-ranges", "none");
		response.setContentLength(contentLenght);
	}
}
