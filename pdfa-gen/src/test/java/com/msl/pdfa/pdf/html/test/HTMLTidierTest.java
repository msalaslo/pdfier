package com.msl.pdfa.pdf.html.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.msl.pdfa.pdf.TestUtil;
import com.msl.pdfa.pdf.html.HTMLTidier;
import com.msl.pdfa.pdf.io.IOUtils;

public class HTMLTidierTest {
	
	@Test
	public void testTidyConfirmationPage(){
		try{					
			InputStream html = TestUtil.getInputHtml();
			File fileOut = new File(TestUtil.getTestPath() + "testTidyConfirmationPage.html");
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
