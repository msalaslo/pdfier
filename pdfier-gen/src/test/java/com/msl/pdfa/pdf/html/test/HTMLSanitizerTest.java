package com.msl.pdfa.pdf.html.test;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.msl.pdfa.pdf.TestUtil;
import com.msl.pdfa.pdf.html.HTMLSanitizer;
import com.msl.pdfa.pdf.io.IOUtils;

import net.htmlparser.jericho.HTMLElementName;

public class HTMLSanitizerTest {
	
	private static final Set<String> HTML_ELEMENTS_TO_STRIP = new HashSet<String>(Arrays.asList(new String[] {
			HTMLElementName.SCRIPT,	HTMLElementName.META,	
		}));
	
	@Test	
	public void testSanitizeConfirmationPage(){
		try{				
			InputStream html = TestUtil.getInputHtml();
			String htmlSanitized = HTMLSanitizer.stripInvalidMarkup(IOUtils.getStringFromInputStream(html),HTML_ELEMENTS_TO_STRIP);

			File file = new File(TestUtil.getTestPath() + "testSanitizeConfirmationPage.html");
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