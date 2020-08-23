package com.msl.pdfier.commons.service;

import java.io.OutputStream;
import java.net.URL;

import com.msl.pdfier.commons.exception.PdfierServiceException;

public interface HtmlToPdfGenenarionService {

	public int generateAndWritePDF(URL sourceUrl, OutputStream out, boolean landScape) throws PdfierServiceException;

	public int generateAndWritePDF(URL sourceUrl, String html, OutputStream out, boolean landScape) throws PdfierServiceException;
}
