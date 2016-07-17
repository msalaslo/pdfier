package com.msl.pdfa.pdf.gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.util.XRLog;

import com.msl.pdfa.pdf.TestUtil;
import com.msl.pdfa.pdf.io.IOUtils;

public class HTMLToPDFConverterTest {
	
	private static Logger logger = LoggerFactory.getLogger(HTMLToPDFConverterTest.class);
	
    @Before
    public void setUp() {
    	System.getProperties().setProperty("xr.util-logging.loggingEnabled", "true");
    	XRLog.setLoggingEnabled(true);
    }

	
	@Test
	public void testReadUrlToPDF(){
		try{
//			URL url = new URL("http://webaim.org/");		 
//			File fileOut = new File(TestUtil.getTestPath() + "webaim.pdf");
			URL url = new URL("https://es.wikipedia.org/wiki/Wikipedia:Portada");		 
			File fileOut = new File(TestUtil.getTestPath() + "wikipedia.pdf");
//			URL url = new URL("http://www.freedomscientific.com/Downloads/JAWS");		 
//			File fileOut = new File(TestUtil.getTestPath() + "JAWS.pdf");
			FileOutputStream outPDF = new FileOutputStream(fileOut);
			
			HTMLToPDFConverter.htmlToPDF(url, outPDF);	
			System.out.println("testConfirmationPageToPDFFlyingJericho:: PDF generado en:" + fileOut.getPath());
			logger.debug(("testConfirmationPageToPDFFlyingJericho:: PDF generado en:" + fileOut.getPath()));
			Assert.assertNotNull("Confirmation PDF Flying Saurce and Jericho with CSS generated not null assertion", outPDF);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}	
	
	@Test
	public void testConfirmationPageToPDF(){
		try{					
			InputStream html = TestUtil.getInputHtml();		  
		
			File fileOut = new File(TestUtil.getTestPath() + "testConfirmationPageToPDF.pdf");
			FileOutputStream outPDF = new FileOutputStream(fileOut);
			
			HTMLToPDFConverter.htmlToPDF(IOUtils.getStringFromInputStream(html), outPDF);	
			System.out.println("testConfirmationPageToPDFFlyingJericho:: PDF generado en:" + fileOut.getPath());
			logger.debug(("testConfirmationPageToPDFFlyingJericho:: PDF generado en:" + fileOut.getPath()));
			Assert.assertNotNull("Confirmation PDF Flying Saurce and Jericho with CSS generated not null assertion", outPDF);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}	
	
		
}
