package com.msl.pdfa.pdf.gen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.msl.pdfa.pdf.commons.Constants;
import com.msl.pdfier.commons.exception.PdfierException;
import com.msl.pdfier.commons.io.IOUtils;
import com.msl.pdfier.commons.pdf.AbstractHtmlToPdfGenerator;

@Component
public class HTMLToPDFConverter extends AbstractHtmlToPdfGenerator {

	private static boolean sanitizePdf = false;

	private static boolean addLocalFonts = false;

	private static Logger logger = LoggerFactory.getLogger(HTMLToPDFConverter.class);

	public static final String[] FONTS = { "ARIAL.TTF" };

	@Override
	public int generatePDFFromHTML(String basePath, String htmlTidied, OutputStream outPDF, String language)
			throws PdfierException {
		ByteArrayOutputStream baos = null;
		ByteArrayOutputStream auxBaos = null;
		int size = 0;
		try {
			ITextRenderer renderer = new ITextRenderer();
			ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(renderer.getOutputDevice());
			callback.setSharedContext(renderer.getSharedContext());
			renderer.getSharedContext().setUserAgentCallback(callback);
			renderer.setPDFVersion(PdfWriter.VERSION_1_7);
			if (addLocalFonts) {
				addFonts(renderer);
			}
			renderer.setDocumentFromString(htmlTidied, basePath);
			renderer.layout();
			if (sanitizePdf) {
				auxBaos = new ByteArrayOutputStream();
				// renderer.createPDF(auxBaos, language);
				renderer.createPDF(auxBaos);
				baos = new ByteArrayOutputStream();
				PDFAccessibleSanitiser.manipulatePdf(new ByteArrayInputStream(auxBaos.toByteArray()), baos);
			} else {
				baos = new ByteArrayOutputStream();
				// renderer.createPDF(baos, language);
				renderer.createPDF(baos);
			}
			size = baos.size();
			baos.writeTo(outPDF);
			return size;
		} catch (Exception e) {
			logger.error("Error generating PDF from html", e);
			throw new PdfierException("Error generating PDF from html", e);
		} finally {
			IOUtils.silentlyCloseOutputStream(baos);
			IOUtils.silentlyCloseOutputStream(auxBaos);
		}
	}

	@Override
	public String getLocalBasePath() {
		String path = null;
		URL baseResource = HTMLToPDFConverter.class.getClassLoader().getResource(Constants.CSS_FILES);
		if (baseResource != null) {
			path = baseResource.getPath();
			int pos = path.indexOf(Constants.CSS_FILES);
			if (pos != -1) {
				path = baseResource.getPath().substring(0, pos);
			}
		}
		return path;
	}

	private static String getFontPath(String fontName) {
		return Constants.FONT_DIR + fontName;
	}

	private static void addFonts(ITextRenderer renderer) throws DocumentException, IOException {
		for (int i = 0; i < FONTS.length; i++) {
			String path = getFontPath(FONTS[i]);
			renderer.getFontResolver().addFont(path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			logger.info("EMBEDDED Font added " + path);
		}
	}
}
