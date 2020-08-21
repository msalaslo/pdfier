package com.msl.pdfier.openpdf.app.service.pdf;

import java.io.OutputStream;
import java.net.URL;

import com.msl.pdfier.openpdf.app.service.pdf.exception.ServiceException;

public interface PDFGenService {
	public int generateAndWritePDF(URL sourceUrl, OutputStream out) throws ServiceException;
	public int generateAndWritePDF(URL sourceUrl, String html, OutputStream out) throws ServiceException;
}
