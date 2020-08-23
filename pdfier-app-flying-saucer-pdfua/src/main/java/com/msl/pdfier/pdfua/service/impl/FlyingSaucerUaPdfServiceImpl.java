package com.msl.pdfier.pdfua.service.impl;

import java.io.OutputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msl.pdfier.commons.exception.PdfierException;
import com.msl.pdfier.commons.service.HtmlToPdfGenenarionService;
import com.msl.pdfier.pdfua.exception.FlyingSaucerPdfUaServiceException;
import com.msl.pdfier.pdfua.gen.pdf.FlyingSaucerPdfUaHTMLToPDFConverter;

@Service
public class FlyingSaucerUaPdfServiceImpl implements HtmlToPdfGenenarionService {

	@Autowired
	FlyingSaucerPdfUaHTMLToPDFConverter hthmlToPdfconverter;

	@Override
	public int generateAndWritePDF(URL sourceUrl, OutputStream out, boolean landScape)
			throws FlyingSaucerPdfUaServiceException {
		try {
			return hthmlToPdfconverter.htmlToPDF(sourceUrl, out, landScape);
		} catch (PdfierException e) {
			throw new FlyingSaucerPdfUaServiceException("Error in generateAndWritePDF", e);
		}
	}

	@Override
	public int generateAndWritePDF(URL sourceUrl, String html, OutputStream out, boolean landScape)
			throws FlyingSaucerPdfUaServiceException {
		try {
			return hthmlToPdfconverter.htmlToPDF(sourceUrl, html, out, landScape);
		} catch (PdfierException e) {
			throw new FlyingSaucerPdfUaServiceException("Error in generateAndWritePDF", e);
		}
	}
}
