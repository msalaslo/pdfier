package com.msl.pdfa.pdf.html.test;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.msl.pdfa.pdf.TestUtil;
import com.msl.pdfa.pdf.commons.Constants;
import com.msl.pdfa.pdf.html.HTMLPrintableUtil;
import com.msl.pdfa.pdf.html.HTMLSanitiser;
import com.msl.pdfa.pdf.io.IOUtils;

import net.htmlparser.jericho.HTMLElementName;

public class HTMLPrintableUtilTest {
	
	private static final Set<String> HTML_ELEMENTS_TO_STRIP = new HashSet<String>(Arrays.asList(new String[] {
			HTMLElementName.SCRIPT,	HTMLElementName.META,	
		}));	
	
	@Test	
	public void testRemoveElementByAttrValuePage(){
		try{				
			InputStream html = TestUtil.getInputHtml();
			String htmlSanitized = HTMLSanitiser.stripInvalidMarkup(IOUtils.getStringFromInputStream(html), HTML_ELEMENTS_TO_STRIP);
			
			//Main test
			String htmlModified = HTMLPrintableUtil.removeElementByAttr(htmlSanitized, "id", "tabPanel2");
			
			File file = new File(TestUtil.getTestPath() + "testRemoveElementByAttrValue.html");
			File resultFile = IOUtils.stringToFile(htmlModified, file);
			System.out.println("HTML with element by attr value removed:" + resultFile.getAbsolutePath());
			Assert.assertNotNull("HTML with element by attr value removed, not null", htmlModified);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test 
	public void testAddInlineCSS() {
		try{			
			InputStream html = TestUtil.getInputHtml();
			String htmlSanitized = HTMLSanitiser.stripInvalidMarkup(IOUtils.getStringFromInputStream(html), Constants.HTML_ELEMENTS_TO_STRIP);
			String htmlCssInLine = HTMLPrintableUtil.addInlineStyleSheets(IOUtils.getInputStream(htmlSanitized), Constants.CSS_FILES);
			
			File file = new File(TestUtil.getTestPath() + "testAddInlineCSS.html");
			
			File resultFile = IOUtils.stringToFile(htmlCssInLine, file);
			System.out.println("HTML Jericho CSS in line saved in:" + resultFile.getAbsolutePath());
			Assert.assertNotNull("HTML Jericho sanitized not null", htmlCssInLine);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}	
	
	@Test 
	public void testMoveStyleToHead() {
		try{			
			InputStream html = TestUtil.getInputHtml();
			String htmlSanitized = HTMLSanitiser.stripInvalidMarkup(IOUtils.getStringFromInputStream(html), Constants	.HTML_ELEMENTS_TO_STRIP);
			String htmlCssInLine = HTMLPrintableUtil.moveStyleToHead(htmlSanitized);
			
			File file = new File(TestUtil.getTestPath() + "testMoveStyleToHead.html");
			
			File resultFile = IOUtils.stringToFile(htmlCssInLine, file);
			System.out.println("HTML Jericho Move css to HEAD saved in:" + resultFile.getAbsolutePath());
			Assert.assertNotNull("HTML Jericho sanitized not null", htmlCssInLine);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
