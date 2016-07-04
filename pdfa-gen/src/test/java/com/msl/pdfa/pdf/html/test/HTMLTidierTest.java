package com.msl.pdfa.pdf.html.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.msl.pdfa.pdf.TestConstants;
import com.msl.pdfa.pdf.html.HTMLTidier;
import com.msl.pdfa.pdf.io.IOUtils;
import com.msl.pdfa.pdf.test.HTMLToPDFConverterTest;

public class HTMLTidierTest {
	
	@Test
	public void testTidyConfirmationPage(){
		try{					
			InputStream html = HTMLTidierTest.class.getClassLoader().getResourceAsStream(TestConstants.PDF_PATH + TestConstants.HTML_CONFIRMATION_PAGE);		  
	        
			String testResourcePath = HTMLToPDFConverterTest.class.getClassLoader().getResource(TestConstants.PDF_PATH + TestConstants.HTML_CONFIRMATION_PAGE).getPath();
			testResourcePath = testResourcePath.substring(0, testResourcePath.indexOf(TestConstants.HTML_CONFIRMATION_PAGE));
		
			File fileOut = new File(testResourcePath + "testTidyConfirmationPage.html");
			FileOutputStream out = new FileOutputStream(fileOut);
			
			String htmlTidied = HTMLTidier.getHTMLTidied(IOUtils.getStringFromInputStream(html));	
			IOUtils.stringToFile(htmlTidied, fileOut);

			System.out.println("testTidyConfirmationPage:: HTML generado en:" + fileOut.getPath());
			Assert.assertNotNull("Confirmation HTML tidied not null assertion", out);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
