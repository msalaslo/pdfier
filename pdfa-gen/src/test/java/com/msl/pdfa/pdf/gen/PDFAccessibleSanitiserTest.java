package com.msl.pdfa.pdf.gen;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.util.XRLog;

import com.msl.pdfa.pdf.TestUtil;

public class PDFAccessibleSanitiserTest {
	
	private static Logger logger = LoggerFactory.getLogger(PDFAccessibleSanitiserTest.class);
	
    @Before
    public void setUp() {
    	System.getProperties().setProperty("xr.util-logging.loggingEnabled", "true");
    	XRLog.setLoggingEnabled(true);
    }

	
	@Test
	public void testManipulatePDF(){
		try{
			String fileInPath = TestUtil.getTestPath() + "pdf/wikipedia-FS-ORI.pdf";
			InputStream is = new FileInputStream(fileInPath);
			Assert.assertNotNull("testManipulatePDF input stream PDF not null assertion with path:" + fileInPath, is);
			
			File fileOut = new File(TestUtil.getTestPath() + "testManipulatePDF.pdf");
			FileOutputStream fos = new FileOutputStream(fileOut);
			PDFAccessibleSanitiser.manipulatePdf(is, fos);	
			fos.close();

			System.out.println("testManipulatePDF:: PDF generado en:" + fileOut.getPath());
			logger.debug(("testManipulatePDF:: PDF generado en:" + fileOut.getPath()));
			Assert.assertNotNull("PDF Flying Saurce testManipulatePDF not null assertion", fos);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}	
	
	
		
}
