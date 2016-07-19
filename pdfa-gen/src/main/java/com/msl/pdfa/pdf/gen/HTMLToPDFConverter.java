package com.msl.pdfa.pdf.gen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.msl.pdfa.pdf.commons.Constants;
import com.msl.pdfa.pdf.exception.UtilException;
import com.msl.pdfa.pdf.html.HTMLPrintableUtil;
import com.msl.pdfa.pdf.html.HTMLTidier;
import com.msl.pdfa.pdf.html.JsoupTidier;
import com.msl.pdfa.pdf.io.IOUtils;

public class HTMLToPDFConverter {

	private static boolean createDebugFiles = true;
	
	private static Logger logger = LoggerFactory.getLogger(HTMLToPDFConverter.class);

	public static final String[] FONTS = { "ARIAL.TTF", "TradeGothicLTStd.ttf", "TradeGothicLTStd-Bold.ttf", "TradeGothicLTStd-Light.ttf", "flysaa-icons.ttf"};

	
	public static void htmlToPDF(URL requestURL, String inputHTML, OutputStream outPDF) throws UtilException {
		try {
			String htmlTidied = HTMLTidier.getHTMLTidied(requestURL, inputHTML);
			Document document= JsoupTidier.parse(htmlTidied);
			String language = JsoupTidier.getLanguage(document);
			htmlTidied = JsoupTidier.getUTF8String(document);
			htmlTidied = HTMLPrintableUtil.replaceNbsp(htmlTidied);
			if(createDebugFiles){
				IOUtils.stringToFile(htmlTidied, new File("c:\\temp\\pdfa-test" + System.currentTimeMillis() + ".html"));
			}
			generatePDFFromHTML(requestURL, htmlTidied, outPDF, language);
		} catch (Exception e) {
			logger.error("Error converting HTML to PDF", e);
			throw new UtilException("Error converting HTML to PDF", e);
		}
	}
	
	public static void htmlToPDF(URL url, OutputStream outPDF) throws UtilException {
		try {
			String htmlTidied = HTMLTidier.getHTMLTidied(url);
			Document document= JsoupTidier.parse(htmlTidied);
			String language = JsoupTidier.getLanguage(document);
			htmlTidied = JsoupTidier.getUTF8String(document);
			htmlTidied = HTMLPrintableUtil.replaceNbsp(htmlTidied);
			htmlTidied = HTMLPrintableUtil.compactSource(htmlTidied);
			htmlTidied = HTMLPrintableUtil.removeBlanksBetweenTags(htmlTidied);
			if(createDebugFiles){
				IOUtils.stringToFile(htmlTidied, new File("c:\\temp\\pdfa-test" + System.currentTimeMillis() + ".html"));
			}
			generatePDFFromHTML(url, htmlTidied, outPDF, language);
		} catch (Exception e) {
			logger.error("Error converting HTML to PDF", e);
			throw new UtilException("Error converting HTML to PDF", e);
		}
	}

	public static void htmlToPDF(String inputHTML, OutputStream outPDF) throws UtilException {
		try {
			String htmlTidied = HTMLTidier.getHTMLTidied(inputHTML);
			Document document= JsoupTidier.parse(htmlTidied);
			String language = JsoupTidier.getLanguage(document);
			htmlTidied = JsoupTidier.getUTF8String(document);
			htmlTidied = HTMLPrintableUtil.replaceNbsp(htmlTidied);
			generateLocalPDFFromHTML(htmlTidied, outPDF, language);
		} catch (Exception e) {
			logger.error("Error converting HTML to PDF", e);
			throw new UtilException("Error converting HTML to PDF", e);
		}
	}

	private static void generatePDFFromHTML(URL sourceUrl, String htmlTidied, OutputStream outPDF, String language) throws UtilException {
		try {
			generateFromHTML(sourceUrl.toString(), htmlTidied, outPDF, language);
		} catch (Exception e) {
			logger.error("Error generating PDF from html", e);
			throw new UtilException("Error generating PDF from html", e);
		}
	}
	
	private static void generateLocalPDFFromHTML(String htmlTidied, OutputStream outPDF, String language) throws UtilException {
		try {
			generateFromHTML(getBasePath(), htmlTidied, outPDF, language);
		} catch (Exception e) {
			logger.error("Error generating PDF from html", e);
			throw new UtilException("Error generating PDF from html", e);
		}
	}
	
	private static void generateFromHTML(String basePath, String htmlTidied, OutputStream outPDF, String language) throws UtilException {
		ByteArrayOutputStream baos = null;
		try {
			ITextRenderer renderer = new ITextRenderer();
			ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(renderer.getOutputDevice());
			callback.setSharedContext(renderer.getSharedContext());
			renderer.getSharedContext().setUserAgentCallback(callback);
			renderer.setPDFVersion(PdfWriter.VERSION_1_7);
			addFonts(renderer);
			renderer.setDocumentFromString(htmlTidied, basePath);
			renderer.layout();
			// Para lanzar el sanitizador de PDF/UA
//			baos = new ByteArrayOutputStream();
//			renderer.createPDF(baos, language, title);
//			PDFAccessibleSanitiser.manipulatePdf(new ByteArrayInputStream(baos.toByteArray()), outPDF);
//			outPDF.close();
			renderer.createPDF(outPDF);
			outPDF.close();
		} catch (Exception e) {
			logger.error("Error generating PDF from html", e);
			throw new UtilException("Error generating PDF from html", e);
		} finally{
			if(baos != null){
				try {
					baos.close();
				} catch (IOException e) {
					// Nothing to do
				}
			}
		}
	}

	private static String getBasePath() {
		String path = null;
		URL baseResource = HTMLToPDFConverter.class.getClassLoader().getResource(Constants.CSS_FILES);
		if (baseResource != null) {
			path = baseResource.getPath();
			int pos = path.indexOf(Constants.CSS_FILES);
			if(pos != -1){
				path = baseResource.getPath().substring(0 , pos);
			}
		}
		return path;
	}

	
	private static String getFontPath(String fontName) {
		return Constants.FONT_DIR + fontName;
	}

	private static void addFonts(ITextRenderer renderer) throws DocumentException, IOException{
		for (int i = 0; i < FONTS.length; i++) {
			String path = getFontPath(FONTS[i]);
			renderer.getFontResolver().addFont(path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			logger.info("EMBEDDED Font added " + path);			
		}
	}	
}
