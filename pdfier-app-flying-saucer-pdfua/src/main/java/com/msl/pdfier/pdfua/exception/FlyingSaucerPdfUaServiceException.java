package com.msl.pdfier.pdfua.exception;

/**
 * Class for handling service layer exceptions in a common way.
 *
 */
public class FlyingSaucerPdfUaServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
    /**
     * Constructs an {@code ServiceException} with the specified detail message and no root cause.
     *
     * @param message
     *            The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */	
	public FlyingSaucerPdfUaServiceException(String message) {
		super(message);
	}

    /**
     * Constructs an {@code ServiceException} with the specified root cause.
     *
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated into this
     * exception's detail message.
     *
     * @param cause
     *            The cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is
     *            permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public FlyingSaucerPdfUaServiceException(Throwable cause) { super(cause); }

    /**
     * Constructs an {@code ServiceException} with the specified detail message and root cause.
     *
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated into this
     * exception's detail message.
     *
     * @param message
     *            The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     *
     * @param cause
     *            The cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is
     *            permitted, and indicates that the cause is nonexistent or unknown.)
     */    
	public FlyingSaucerPdfUaServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
}