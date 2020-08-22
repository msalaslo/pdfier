package com.msl.pdfier.pdfua.gen.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

public class PDFAccessibleSanitiser {

	public static void manipulatePdf(InputStream src, OutputStream dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfDictionary catalog = reader.getCatalog();
		PdfDictionary structTreeRoot = catalog.getAsDict(PdfName.STRUCTTREEROOT);
		manipulate(structTreeRoot);
		PdfStamper stamper = new PdfStamper(reader, dest);
		stamper.close();
	}

	private static void manipulate(PdfDictionary element) {
		if (element == null)
			return;
		// Remove Empty LI structure elements
		if (PdfName.LI.equals(element.get(PdfName.S))) {
			//If has no Kids is an empty LI
			if(element.get(PdfName.K) == null){
				element.clear();
			}
		}else if (PdfName.FIGURE.equals(element.get(PdfName.S))) {
			if(element.get(PdfName.ALT) == null || (element.get(PdfName.ALT) != null && element.get(PdfName.ALT).length() < 1)){
				element.put(PdfName.ALT, new PdfString("Figure without an Alt description"));
			}
	    }
		PdfArray kids = element.getAsArray(PdfName.K);
		if (kids == null)
			return;
		for (int i = 0; i < kids.size(); i++)
			manipulate(kids.getAsDict(i));
	}
	
	/**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException 
     */
    public static void manipulateImagesPdf(InputStream src, OutputStream dest) throws IOException, DocumentException {
        // Read the file
        PdfReader reader = new PdfReader(src);
        int n = reader.getXrefSize();
        PdfObject object;
        PRStream stream;
        // Look for image and manipulate image stream
        for (int i = 0; i < n; i++) {
            object = reader.getPdfObject(i);
            if (object == null || !object.isStream())
                continue;
            stream = (PRStream)object;
            
            System.out.println("stream:" + stream);
            System.out.println("stream keys:" + stream.getKeys());
        }
        // Save altered PDF
        PdfStamper stamper = new PdfStamper(reader, dest);
        stamper.close();
        reader.close();
    }
}
