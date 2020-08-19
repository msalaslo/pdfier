package com.msl.pdfa.pdf.html;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msl.pdfa.pdf.commons.Constants;
import com.msl.pdfa.pdf.exception.PdfUAGenerationException;
import com.msl.pdfa.pdf.http.HTTPClient;

public class HTMLTidier {
	
	protected static final Logger logger = LoggerFactory.getLogger(HTMLTidier.class);

	public static String getHTMLTidied(URL sourceUrl) throws PdfUAGenerationException {
		try {
			String htmlTidied = "";
			try {
				htmlTidied = HTTPClient.readUrlForPdf(sourceUrl.toURI());
			} catch (Exception ex) {
				logger.warn("Error adding external CSS to inline doc." + ex.getMessage());
			}
			htmlTidied = HTMLSanitizer.stripInvalidMarkup(htmlTidied, Constants.HTML_ELEMENTS_TO_STRIP);
			htmlTidied = HTMLPrintableUtil.moveStyleToHead(htmlTidied);
			htmlTidied = HTMLPrintableUtil.addExternalInlineStyleSheets(sourceUrl, htmlTidied);	
			htmlTidied = HTMLPrintableUtil.addCDATAToHeadStyleTags(htmlTidied);	
			return htmlTidied;
		} catch (Exception e) {
			logger.error("Error tidying HTML", e);
			throw new PdfUAGenerationException("Error tidying HTML", e);
		}
	}
	
	public static String getHTMLTidied(URL requestUrl, String inputHTML) throws PdfUAGenerationException {
		try {
			String htmlTidied = HTMLSanitizer.stripInvalidMarkup(inputHTML, Constants.HTML_ELEMENTS_TO_STRIP);
			htmlTidied = HTMLPrintableUtil.addMandatoryHtml(htmlTidied);	
			htmlTidied = HTMLPrintableUtil.moveStyleToHead(htmlTidied);
			htmlTidied = HTMLPrintableUtil.addExternalInlineStyleSheets(requestUrl, htmlTidied);
			return htmlTidied;
		} catch (Exception e) {
			logger.error("Error tidying HTML", e);
			throw new PdfUAGenerationException("Error tidying HTML", e);
		}
	}

	public static String getHTMLTidied(String inputHTML) throws PdfUAGenerationException {
		try {
			String htmlTidied = HTMLSanitizer.stripInvalidMarkup(inputHTML, Constants.HTML_ELEMENTS_TO_STRIP);
			htmlTidied = HTMLPrintableUtil.addMandatoryHtml(htmlTidied);	
			htmlTidied = HTMLPrintableUtil.moveStyleToHead(htmlTidied);;	
//			htmlTidied = HTMLPrintableUtil.addInlineStyleSheets(IOUtils.getInputStream(htmlTidied), CSS_FILES);		
			return htmlTidied;
		} catch (Exception e) {
			logger.error("Error tidying HTML", e);
			throw new PdfUAGenerationException("Error tidying HTML", e);
		}
	}

}
