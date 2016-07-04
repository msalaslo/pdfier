package com.msl.pdfa.pdf.html.test;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.htmlparser.jericho.HTMLElementName;

import org.junit.Assert;
import org.junit.Test;

import com.msl.pdfa.pdf.html.HTMLSanitiser;
import com.msl.pdfa.pdf.io.IOUtils;
import com.msl.pdfa.pdf.test.HTMLToPDFConverterTest;

public class HTMLSanitiserTest {
	private static final String PDF_PATH = "pdf/";
	private static final String HTML_CONFIRMATION_PAGE = "confirmation-page.html";
	private static final Set<String> HTML_ELEMENTS_TO_STRIP = new HashSet<String>(Arrays.asList(new String[] {
			HTMLElementName.SCRIPT,	HTMLElementName.META,	
		}));
	
	@Test	
	public void testSanitizeConfirmationPage(){
		try{				
			InputStream html = HTMLSanitiserTest.class.getClassLoader().getResourceAsStream(PDF_PATH + HTML_CONFIRMATION_PAGE);
			String htmlSanitized = HTMLSanitiser.stripInvalidMarkup(IOUtils.getStringFromInputStream(html),HTML_ELEMENTS_TO_STRIP);
			
			String testResourcePath = HTMLToPDFConverterTest.class.getClassLoader().getResource(PDF_PATH + HTML_CONFIRMATION_PAGE).getPath();
			testResourcePath = testResourcePath.substring(0, testResourcePath.indexOf(HTML_CONFIRMATION_PAGE));
			File file = new File(testResourcePath + "testSanitizeConfirmationPage.html");
			File resultFile = IOUtils.stringToFile(htmlSanitized, file);
			System.out.println("HTML Jericho sanitized:" + htmlSanitized);
			System.out.println("HTML Jericho sanitized saved in:" + resultFile.getAbsolutePath());
			Assert.assertNotNull("HTML Jericho sanitized not null", htmlSanitized);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
}