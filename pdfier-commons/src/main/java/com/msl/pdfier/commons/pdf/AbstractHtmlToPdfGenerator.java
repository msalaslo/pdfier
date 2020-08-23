package com.msl.pdfier.commons.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msl.pdfier.commons.exception.PdfierException;
import com.msl.pdfier.commons.html.HTMLPrintableUtil;
import com.msl.pdfier.commons.html.HTMLTidier;
import com.msl.pdfier.commons.html.JsoupTidier;
import com.msl.pdfier.commons.io.IOUtils;

public abstract class AbstractHtmlToPdfGenerator {

	public boolean createDebugFiles = false;

	public String debugTempFilesPath = "c:\\temp\\pdfier-";

	private static Logger logger = LoggerFactory.getLogger(AbstractHtmlToPdfGenerator.class);

	public int htmlToPDF(URL url, String inputHTML, OutputStream outPDF, boolean landScape) throws PdfierException {
		try {
			String htmlTidied = HTMLTidier.getHTMLTidied(url, inputHTML);
			Document document = JsoupTidier.parse(htmlTidied);
			JsoupTidier.addHTMLNameSpaces(document);
			if(landScape) {
				JsoupTidier.addLandScapeOrientation(document);
			}
			String language = JsoupTidier.getLanguage(document);
			htmlTidied = JsoupTidier.getUTF8String(document);
			htmlTidied = HTMLPrintableUtil.replaceNbsp(htmlTidied);
			if (createDebugFiles) {
				IOUtils.stringToFile(htmlTidied, new File(debugTempFilesPath + System.currentTimeMillis() + ".html"));
			}
			return generatePDFFromHTML(getBaseUrl(url), htmlTidied, outPDF, language);
		} catch (Exception e) {
			logger.error("Error converting HTML to PDF", e);
			throw new PdfierException("Error converting HTML to PDF", e);
		}
	}

	public int htmlToPDF(URL url, OutputStream outPDF, boolean landScape) throws PdfierException {
		try {
			String htmlTidied = HTMLTidier.getHTMLTidied(url);
			Document document = JsoupTidier.parse(htmlTidied);
			JsoupTidier.addHTMLNameSpaces(document);
			if(landScape) {
				JsoupTidier.addLandScapeOrientation(document);
			}
			String language = JsoupTidier.getLanguage(document);
			htmlTidied = JsoupTidier.getUTF8String(document);
			htmlTidied = HTMLPrintableUtil.replaceNbsp(htmlTidied);
			htmlTidied = HTMLPrintableUtil.compactSource(htmlTidied);
			htmlTidied = HTMLPrintableUtil.removeBlanksBetweenTags(htmlTidied);
			if (createDebugFiles) {
				IOUtils.stringToFile(htmlTidied, new File(debugTempFilesPath + System.currentTimeMillis() + ".html"));
			}
			return generatePDFFromHTML(getBaseUrl(url), htmlTidied, outPDF, language);
		} catch (Exception e) {
			logger.error("Error converting HTML to PDF", e);
			throw new PdfierException("Error converting HTML to PDF", e);
		}
	}

	public int htmlToPDF(String inputHTML, OutputStream outPDF, boolean landScape) throws PdfierException {
		try {
			String htmlTidied = HTMLTidier.getHTMLTidied(inputHTML);
			Document document = JsoupTidier.parse(htmlTidied);
			JsoupTidier.addHTMLNameSpaces(document);
			if(landScape) {
				JsoupTidier.addLandScapeOrientation(document);
			}
			String language = JsoupTidier.getLanguage(document);
			htmlTidied = JsoupTidier.getUTF8String(document);
			htmlTidied = HTMLPrintableUtil.replaceNbsp(htmlTidied);
			return generateLocalPDFFromHTML(htmlTidied, outPDF, language);
		} catch (Exception e) {
			logger.error("Error converting HTML to PDF", e);
			throw new PdfierException("Error converting HTML to PDF", e);
		}
	}

	private int generateLocalPDFFromHTML(String htmlTidied, OutputStream outPDF, String language)
			throws PdfierException {
		try {
			return generatePDFFromHTML(getLocalBasePath(), htmlTidied, outPDF, language);
		} catch (Exception e) {
			logger.error("Error generating PDF from html", e);
			throw new PdfierException("Error generating PDF from html", e);
		}
	}

	/**
	 * This is the core method of the class. Generate PDF from a XHTML tidied
	 * 
	 * @param basePath   base URI to retrieve additional resources like CSS, images,
	 *                   fonts, etc.
	 * @param htmlTidied the origin HTML
	 * @param outPDF     the outputStream to write the PDF
	 * @param language   document language
	 * @return size of the result PDF
	 * @throws Exception
	 */
	public abstract int generatePDFFromHTML(String basePath, String htmlTidied, OutputStream outPDF, String language)
			throws PdfierException;

	public abstract String getLocalBasePath();

	public void saveToFile(ByteArrayOutputStream baos) {
		FileOutputStream fos = null;
		String filePath = debugTempFilesPath + this.getClass().getSimpleName() + System.currentTimeMillis() + ".pdf";
		logger.info("Saving PDF in:" + filePath);
		try {
			fos = new FileOutputStream(new File(filePath));
			baos.writeTo(fos);
		} catch (IOException e) {
			logger.warn("Error saving debug file", e);
		} finally {
			IOUtils.silentlyCloseOutputStream(fos);
		}

	}

	public static String getBaseUrl(URL url) {
		if (url == null) {
			return null;
		}
		try {
			return url.getProtocol() + "://" + url.getAuthority() + "/";
		} catch (Exception e) {
			return null;
		}
	}

}
