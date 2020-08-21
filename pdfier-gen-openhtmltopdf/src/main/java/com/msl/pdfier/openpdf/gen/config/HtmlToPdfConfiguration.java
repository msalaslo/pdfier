package com.msl.pdfier.openpdf.gen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "html-to-pdf")
public class HtmlToPdfConfiguration {

	private boolean pdfUaAccessibility;
	private boolean saveGeneratedPDF;
	private boolean addLocalFonts;

	public boolean isPdfUaAccessibility() {
		return pdfUaAccessibility;
	}

	public void setPdfUaAccessibility(boolean pdfUaAccessibility) {
		this.pdfUaAccessibility = pdfUaAccessibility;
	}

	public boolean isSaveGeneratedPDF() {
		return saveGeneratedPDF;
	}

	public void setSaveGeneratedPDF(boolean saveGeneratedPDF) {
		this.saveGeneratedPDF = saveGeneratedPDF;
	}

	public boolean isAddLocalFonts() {
		return addLocalFonts;
	}

	public void setAddLocalFonts(boolean addLocalFonts) {
		this.addLocalFonts = addLocalFonts;
	}

}
