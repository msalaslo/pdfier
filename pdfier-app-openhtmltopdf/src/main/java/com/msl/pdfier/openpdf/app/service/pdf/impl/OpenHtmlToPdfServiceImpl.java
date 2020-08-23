package com.msl.pdfier.openpdf.app.service.pdf.impl;

import java.io.OutputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msl.pdfier.commons.exception.PdfierException;
import com.msl.pdfier.commons.service.HtmlToPdfGenenarionService;
import com.msl.pdfier.openpdf.exception.OpenHtmlToPdfServiceException;
import com.msl.pdfier.openpdf.gen.pdf.OpenHtmlToPdfConverter;

@Service
public class OpenHtmlToPdfServiceImpl implements HtmlToPdfGenenarionService {

	@Autowired
	OpenHtmlToPdfConverter hthmlToPdfconverter;

	@Override
	public int generateAndWritePDF(URL sourceUrl, OutputStream out, boolean landScape) throws OpenHtmlToPdfServiceException {
		try {
			return hthmlToPdfconverter.htmlToPDF(sourceUrl, out, landScape);
		} catch (PdfierException e) {
			throw new OpenHtmlToPdfServiceException("Error in generateAndWritePDF", e);
		}
	}

	@Override
	public int generateAndWritePDF(URL sourceUrl, String html, OutputStream out, boolean landScape) throws OpenHtmlToPdfServiceException {
		try {
			return hthmlToPdfconverter.htmlToPDF(sourceUrl, html, out,landScape);
		} catch (PdfierException e) {
			throw new OpenHtmlToPdfServiceException("Error in generateAndWritePDF", e);
		}
	}

}
