package com.pdfier.service.pdf.impl;

import java.io.OutputStream;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.msl.pdfa.pdf.exception.PdfUAGenerationException;
import com.msl.pdfa.pdf.gen.HTMLToPDFConverter;
import com.pdfier.service.exception.ServiceException;
import com.pdfier.service.pdf.PDFGenService;

@Service
public class PDFGenServiceImpl implements PDFGenService {

	@Override
	public int generateAndWritePDF(URL sourceUrl, OutputStream out) throws ServiceException {
		try {
			return HTMLToPDFConverter.htmlToPDF(sourceUrl, out);
		} catch (PdfUAGenerationException e) {
			throw new ServiceException("Error in generateAndWritePDF", e);
		}
	}

	@Override
	public int generateAndWritePDF(URL sourceUrl, String html, OutputStream out) throws ServiceException {
		try {
			return HTMLToPDFConverter.htmlToPDF(sourceUrl, html, out);
		} catch (PdfUAGenerationException e) {
			throw new ServiceException("Error in generateAndWritePDF", e);
		}
	}
}
