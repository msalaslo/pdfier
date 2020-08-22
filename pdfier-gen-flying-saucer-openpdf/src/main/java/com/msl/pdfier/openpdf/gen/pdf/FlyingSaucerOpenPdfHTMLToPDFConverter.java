package com.msl.pdfier.openpdf.gen.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.msl.pdfier.commons.exception.PdfierException;
import com.msl.pdfier.commons.io.IOUtils;
import com.msl.pdfier.commons.pdf.AbstractHtmlToPdfGenerator;
import com.msl.pdfier.openpdf.gen.commons.Constants;
import com.msl.pdfier.openpdf.gen.config.HtmlToPdfConfiguration;

@Component
public class FlyingSaucerOpenPdfHTMLToPDFConverter extends AbstractHtmlToPdfGenerator{

	private static Logger logger = LoggerFactory.getLogger(FlyingSaucerOpenPdfHTMLToPDFConverter.class);
	
	@Autowired
	private HtmlToPdfConfiguration configuration;

	public static final String[] FONTS = { "ARIAL.TTF" };

	public int generatePDFFromHTML(String basePath, String htmlTidied, OutputStream outPDF, String language) throws PdfierException {
		ByteArrayOutputStream baos = null;
		ByteArrayOutputStream auxBaos = null;
		int size = 0;
		try {
			ITextRenderer renderer = new ITextRenderer();
			ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(renderer.getOutputDevice());
			callback.setSharedContext(renderer.getSharedContext());
			renderer.getSharedContext().setUserAgentCallback(callback);
			renderer.setPDFVersion(PdfWriter.VERSION_1_7);
			if(configuration.isAddLocalFonts()) {
				addFonts(renderer);
			}
			renderer.setDocumentFromString(htmlTidied, basePath);
			renderer.layout();
			if(configuration.isSanitizePdf()){
				auxBaos = new ByteArrayOutputStream();
				//renderer.createPDF(auxBaos, language);
				renderer.createPDF(auxBaos);
				baos = new ByteArrayOutputStream();
				PDFAccessibleSanitizer.manipulatePdf(new ByteArrayInputStream(auxBaos.toByteArray()), baos);
			}else{
				baos = new ByteArrayOutputStream();
				//renderer.createPDF(baos, language);
				renderer.createPDF(baos);
			}
			size = baos.size();
			baos.writeTo(outPDF);
			if(configuration.isSaveGeneratedPdf()) {
				saveToFile(baos);
			}
			return size;
		} catch (Exception e) {
			logger.error("Error generating PDF from html", e);
			throw new PdfierException("Error generating PDF from html", e);
		} finally{
			IOUtils.silentlyCloseOutputStream(baos);
			IOUtils.silentlyCloseOutputStream(auxBaos);
		}
	}

	public String getLocalBasePath() {
		String path = null;
		URL baseResource = FlyingSaucerOpenPdfHTMLToPDFConverter.class.getClassLoader().getResource(Constants.CSS_FILES);
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
