package com.msl.pdfier.pdfua.gen.itext.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.msl.pdfier.pdfua.gen.utils.test.TestUtil;

public class PdfFormTest {

	public static void main(String[] args) {
		try {
			String outPath = TestUtil.getTestPath() + "PdfFormTest.pdf";
			File file = new File(outPath);
			file.getParentFile().mkdirs();
			new PdfFormTest().createPdf(outPath);
			System.out.println("PDF con formulario creado en:" + outPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createPdf(String dest) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
	    writer.setPdfVersion(PdfWriter.VERSION_1_7);
	    //TAGGED PDF
	    //Make document tagged
	    writer.setTagged();
	    //===============
	    //PDF/UA
	    //Set document metadata
	    writer.setViewerPreferences(PdfWriter.DisplayDocTitle);
	    document.addLanguage("en-US");
	    document.addTitle("English pangram");
	    writer.createXmpMetadata();
	    //=====================
		document.open();
		PushbuttonField button = new PushbuttonField(writer, new Rectangle(36, 780, 144, 806), "textoboton");
		button.setText("Hola");
		writer.addAnnotation(button.getField());
		document.close();
	}
}
