package com.msl.pdfier.openpdf.gen.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "html-to-pdf-generation", ignoreUnknownFields = false)
public class HtmlToPdfConfiguration {

	@NotNull
	private boolean pdfUaAccessibility;
	
	@NotNull
	private boolean saveGeneratedPdf;
	
	@NotNull
	private boolean addLocalFonts;
	
	public boolean isPdfUaAccessibility() {
		return pdfUaAccessibility;
	}

	public void setPdfUaAccessibility(boolean pdfUaAccessibility) {
		this.pdfUaAccessibility = pdfUaAccessibility;
	}

	public boolean isSaveGeneratedPdf() {
		return saveGeneratedPdf;
	}

	public void setSaveGeneratedPdf(boolean saveGeneratedPdf) {
		this.saveGeneratedPdf = saveGeneratedPdf;
	}

	public boolean isAddLocalFonts() {
		return addLocalFonts;
	}

	public void setAddLocalFonts(boolean addLocalFonts) {
		this.addLocalFonts = addLocalFonts;
	}

}
