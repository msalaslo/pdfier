package com.msl.pdfa.pdf.exception;

public class PdfUAGenerationException extends Exception {

	private static final long serialVersionUID = -2033349219617163882L;

	public PdfUAGenerationException() {	
	}

	public PdfUAGenerationException(String message) {
		super(message);	
	}

	public PdfUAGenerationException(Throwable cause) {
		super(cause);		
	}

	public PdfUAGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PdfUAGenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);		
	}
}
