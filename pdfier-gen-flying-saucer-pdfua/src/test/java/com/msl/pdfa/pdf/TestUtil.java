package com.msl.pdfa.pdf;

import java.io.InputStream;

public class TestUtil {

	private static String testPath;
	private static InputStream inputHtml;
	
	public static String getTestPath(){
		if(testPath == null){
			String testResourcePath = TestUtil.class.getClassLoader().getResource(TestConstants.PDF_PATH + TestConstants.HTML_CONFIRMATION_PAGE).getPath();
			testPath=  testResourcePath.substring(0, testResourcePath.indexOf(TestConstants.HTML_CONFIRMATION_PAGE));
		}
		return testPath;
	}
	
	public static InputStream getInputHtml(){
		if(inputHtml == null){
			inputHtml = TestUtil.class.getClassLoader().getResourceAsStream(TestConstants.PDF_PATH + TestConstants.HTML_CONFIRMATION_PAGE);
		}
		return inputHtml;
	}
}
