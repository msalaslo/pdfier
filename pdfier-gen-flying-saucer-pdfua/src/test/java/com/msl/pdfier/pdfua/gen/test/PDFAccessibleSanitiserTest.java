package com.msl.pdfier.pdfua.gen.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.util.XRLog;

import com.msl.pdfier.pdfua.gen.pdf.PDFAccessibleSanitiser;
import com.msl.pdfier.pdfua.gen.utils.test.TestUtil;

public class PDFAccessibleSanitiserTest {
	
	private static Logger logger = LoggerFactory.getLogger(PDFAccessibleSanitiserTest.class);
	
    public void setUp() {
    	System.getProperties().setProperty("xr.util-logging.loggingEnabled", "true");
    	XRLog.setLoggingEnabled(true);
    }

	
	@Test
	public void testManipulatePDF(){
		try{
			setUp();
			String fileInPath = TestUtil.getTestPath() + "pdf/wikipedia-FS-ORI.pdf";
			InputStream is = new FileInputStream(fileInPath);
			assertNotNull(is, "testManipulatePDF input stream PDF not null assertion with path:" + fileInPath);
			
			File fileOut = new File(TestUtil.getTestPath() + "testManipulatePDF.pdf");
			FileOutputStream fos = new FileOutputStream(fileOut);
			PDFAccessibleSanitiser.manipulatePdf(is, fos);	
			fos.close();

			System.out.println("testManipulatePDF:: PDF generado en:" + fileOut.getPath());
			logger.debug(("testManipulatePDF:: PDF generado en:" + fileOut.getPath()));
			assertNotNull(fos, "PDF Flying Saurce testManipulatePDF not null assertion");
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	
		
}
