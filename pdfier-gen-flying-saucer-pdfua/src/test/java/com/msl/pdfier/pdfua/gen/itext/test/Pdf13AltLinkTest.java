package com.msl.pdfier.pdfua.gen.itext.test;

import java.io.FileOutputStream;
import java.net.URL;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfStructureTreeRoot;
import com.itextpdf.text.pdf.PdfWriter;
import com.msl.pdfier.pdfua.gen.utils.test.TestUtil;

public class Pdf13AltLinkTest {
	
	public static void main(String[] args) {
	    try {
	      Document document = new Document(PageSize.LETTER);
	       
	      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(TestUtil.getTestPath() + "Pdf13AltLink.pdf"));
	 
	      Rectangle rect = document.getPageSize();
	       
	      writer.setTagged();
	 
	      document.open();
	 
	      PdfContentByte cb = writer.getDirectContent();
	      PdfStructureTreeRoot rootElement = writer.getStructureTreeRoot();
	 
//	      DocumentStructure documentStructure = new DocumentStructure(rootElement, "en-ca"); // for english canadian
//	      PageStructure pageStructure = documentStructure.getPage(0);
	      PdfStructureElement paragraphElement = new PdfStructureElement(rootElement, PdfName.P);
	 
	      float llx = 50; 
	      float lly = rect.getTop() - 200; 
	      float urx = 300; 
	      float ury = rect.getTop() - 100; 
	      ColumnText ct = new ColumnText(cb); 
	      ct.setSimpleColumn(llx, lly, urx, ury); 
	      Chunk chunk = new Chunk("Web Site for iText WCAG 2.0 PDF Information"); 
	      PdfAction action = new PdfAction(new URL("http://pdfa.com")); 
	      chunk.setAction(action);
	      ct.addText(chunk);  
	      PdfStructureElement linkElement = new PdfStructureElement(paragraphElement, PdfName.LINK); 
	      PdfDictionary dict = new PdfDictionary();
	      dict.put(PdfName.OBJR, linkElement);
	      cb.beginMarkedContentSequence(PdfName.OBJR, dict, true); 
	       
	      
	      dict.put(PdfName.ALT, new PdfString("This link goes to discotek.ca, provides information about how to implement WCAG 2.0 with itext."));
	      cb.beginMarkedContentSequence(PdfName.SPAN, dict, true);
	       
	      ct.go(); 
	       
	      cb.endMarkedContentSequence(); // span/alt 
	      cb.endMarkedContentSequence(); // link element
	 
	      document.close();
	    } 
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
}
