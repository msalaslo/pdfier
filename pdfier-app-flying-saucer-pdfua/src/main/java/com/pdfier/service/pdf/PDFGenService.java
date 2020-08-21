package com.pdfier.service.pdf;

import java.io.OutputStream;
import java.net.URL;

import com.pdfier.service.exception.ServiceException;

public interface PDFGenService {

	public int generateAndWritePDF(URL sourceUrl, OutputStream out) throws ServiceException;

	public int generateAndWritePDF(URL sourceUrl, String html, OutputStream out) throws ServiceException;
}
