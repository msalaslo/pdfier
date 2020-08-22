package com.pdfier.service.pdf.impl;

import java.io.OutputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msl.pdfier.commons.exception.PdfierException;
import com.msl.pdfier.pdfua.gen.exception.PdfUAGenerationException;
import com.msl.pdfier.pdfua.gen.pdf.FlyingSaucerPdfUaHTMLToPDFConverter;
import com.pdfier.service.exception.ServiceException;
import com.pdfier.service.pdf.PDFGenService;

@Service
public class PDFGenServiceImpl implements PDFGenService {
	
	@Autowired
	FlyingSaucerPdfUaHTMLToPDFConverter hthmlToPdfconverter;

	@Override
	public int generateAndWritePDF(URL sourceUrl, OutputStream out) throws ServiceException {
		try {
			return hthmlToPdfconverter.htmlToPDF(sourceUrl, out);
		} catch (PdfierException e) {
			throw new ServiceException("Error in generateAndWritePDF", e);
		}
	}

	@Override
	public int generateAndWritePDF(URL sourceUrl, String html, OutputStream out) throws ServiceException {
		try {
			return hthmlToPdfconverter.htmlToPDF(sourceUrl, html, out);
		} catch (PdfierException e) {
			throw new ServiceException("Error in generateAndWritePDF", e);
		}
	}
}
