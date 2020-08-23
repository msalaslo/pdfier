package com.msl.pdfier.openpdf.service.impl;

import java.io.OutputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msl.pdfier.commons.exception.PdfierException;
import com.msl.pdfier.commons.exception.PdfierServiceException;
import com.msl.pdfier.commons.service.HtmlToPdfGenenarionService;
import com.msl.pdfier.openpdf.exception.FlyingSaucerOpenPdfServiceException;
import com.msl.pdfier.openpdf.gen.pdf.FlyingSaucerOpenPdfHTMLToPDFConverter;

@Service
public class FlyingSaucerOpenPdfServiceImpl implements HtmlToPdfGenenarionService {
	
	@Autowired
	FlyingSaucerOpenPdfHTMLToPDFConverter hthmlToPdfconverter;

	@Override
	public int generateAndWritePDF(URL sourceUrl, OutputStream out, boolean landScape) throws PdfierServiceException {
		try {
			return hthmlToPdfconverter.htmlToPDF(sourceUrl, out, landScape);
		} catch (PdfierException e) {
			throw new FlyingSaucerOpenPdfServiceException("Error in generateAndWritePDF", e);
		}
	}

	@Override
	public int generateAndWritePDF(URL sourceUrl, String html, OutputStream out, boolean landScape) throws PdfierServiceException {
		try {
			return hthmlToPdfconverter.htmlToPDF(sourceUrl, html, out, landScape);
		} catch (PdfierException e) {
			throw new FlyingSaucerOpenPdfServiceException("Error in generateAndWritePDF", e);
		}
	}

}
