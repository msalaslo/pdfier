package com.msl.pdfa.pdf.gen;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

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
		}
		PdfArray kids = element.getAsArray(PdfName.K);
		if (kids == null)
			return;
		for (int i = 0; i < kids.size(); i++)
			manipulate(kids.getAsDict(i));
	}

}
