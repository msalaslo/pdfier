package com.msl.pdfier.openpdf.gen.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.msl.pdfier.commons.exception.PdfierException;
import com.msl.pdfier.commons.io.IOUtils;
import com.msl.pdfier.commons.pdf.AbstractHtmlToPdfGenerator;
import com.msl.pdfier.openpdf.gen.commons.Constants;
import com.msl.pdfier.openpdf.gen.config.HtmlToPdfConfiguration;
import com.openhtmltopdf.mathmlsupport.MathMLDrawer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;

@Component
public class OpenPdfHTMLToPDFConverter extends AbstractHtmlToPdfGenerator{
		
	private Logger logger = LoggerFactory.getLogger(OpenPdfHTMLToPDFConverter.class);

	public static final String[] FONTS = { "ARIAL.TTF" };
	
	@Autowired
	HtmlToPdfConfiguration configuration;


	/**
	 * This is the core method of the class. Generate PDF from a XHTML tidied
	 * @param basePath base URI to retrieve additional resources like CSS, images, fonts, etc.
	 * @param htmlTidied the origin HTML
	 * @param outPDF the outputStream to write the PDF
	 * @param language document language
	 * @return size of the result PDF
	 * @throws Exception
	 */
	@Override
	public int generatePDFFromHTML(String basePath, String htmlTidied, OutputStream outPDF, String language)
			throws PdfierException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int size = 0;
		try {
			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.useFastMode();
			
			//Activate PDF/UA
			builder.usePdfUaAccessbility(configuration.isPdfUaAccessibility());
			
			// Set OkHttp Client recommended
			builder.useHttpStreamImplementation(new OkHttpStreamFactory());

			// For SVG support
			builder.useSVGDrawer(new BatikSVGDrawer());

			// For MathML support
			builder.useMathMLDrawer(new MathMLDrawer());

			// Add local fonts, if needed
			if (configuration.isAddLocalFonts()) {
				addFonts(builder);
			}

			builder.withHtmlContent(htmlTidied, basePath);
			builder.toStream(baos);
			builder.run();

			// From byteArray to OutputStream
			size = baos.size();
			baos.writeTo(outPDF);
			if(configuration.isSaveGeneratedPDF()) {
				saveToFile(baos);
			}
			return size;
		} catch (Exception e) {
			logger.error("Error generating PDF from html", e);
			throw new PdfierException("Error generating PDF from html", e);
		} finally {
			IOUtils.silentlyCloseOutputStream(baos);
		}
	}

	@Override
	public String getLocalBasePath() {
		String path = null;
		URL baseResource = OpenPdfHTMLToPDFConverter.class.getClassLoader().getResource(Constants.CSS_FILES);
		if (baseResource != null) {
			path = baseResource.getPath();
			int pos = path.indexOf(Constants.CSS_FILES);
			if (pos != -1) {
				path = baseResource.getPath().substring(0, pos);
			}
		}
		return path;
	}

	private String getFontPath(String fontName) {
		return Constants.FONT_DIR + fontName;
	}

	private void addFonts(PdfRendererBuilder builder) throws IOException {
		for (int i = 0; i < FONTS.length; i++) {
			String path = getFontPath(FONTS[i]);
			// Uses the shorthand method, assumes normal weight and style and subset set to
			// true.
			builder.useFont(new File(path), "hand");
			// Longhand method
//			builder.useFont(new File(path), "arabic-bold-italic", 700, FontStyle.ITALIC, /* subset: */ true);
			logger.info("EMBEDDED Font added " + path);
		}
	}

}
