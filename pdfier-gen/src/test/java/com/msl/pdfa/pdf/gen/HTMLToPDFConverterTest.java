package com.msl.pdfa.pdf.gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
		System.getProperties().setProperty("xr.util-logging.loggingEnabled", "false");
		XRLog.setLoggingEnabled(true);
	}

	@Test
	public void testReadUrlToPDF() {
		try {

			// URL url = new
			// URL("http://www.autocontrol.es/reclamaciones_online.aspx");
			// File fileOut = new File(TestUtil.getTestPath() +
			// "autocontrol.pdf");
//			 URL url = new URL("http://webaim.org/training/");
//			 File fileOut = new File(TestUtil.getTestPath() + "webaim.pdf");
//			URL url = new URL("https://es.wikipedia.org/wiki/Wikipedia:Portada");
//			File fileOut = new File(TestUtil.getTestPath() + "wikipedia.pdf");
			 URL url = new
			 URL("https://donate.wikimedia.org/w/index.php?title=Special:LandingPage&country=ES&uselang=es&utm_medium=sidebar&utm_source=donate&utm_campaign=C13_es.wikipedia.org");
			 File fileOut = new File(TestUtil.getTestPath() + "wiki-donaciones.pdf");
			FileOutputStream outPDF = new FileOutputStream(fileOut);

			int size = HTMLToPDFConverter.htmlToPDF(url, outPDF);
			System.out.println("PdfUA Generation:: path:" + fileOut.getPath() + ", tamaño:" + size);
			logger.debug(("PdfUA Generation:: path:" + fileOut.getPath()) + ", tamaño:" + size);
			Assert.assertNotNull("PdfUA Generation:: not null assertion", outPDF);
			Assert.assertTrue("PdfUA Generation:: size bigger than 0 assertion", size > 0);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testAll() {
		Set<File> keys = null;
		try {
			Map<File, URL> testWebs = new HashMap<>();
			testWebs.put(new File(TestUtil.getTestPath() + "webaim.pdf"), new URL("https://webaim.org/training/"));
			testWebs.put(new File(TestUtil.getTestPath() + "wikipedia.pdf"), new URL("https://es.wikipedia.org/wiki/Wikipedia:Portada"));
//			testWebs.put(new File(TestUtil.getTestPath() + "JAWS.pdf"), new URL("http://www.freedomscientific.com/Downloads/JAWS"));
			keys = testWebs.keySet();
			for (File file : keys) {
				int size = HTMLToPDFConverter.htmlToPDF(testWebs.get(file), new FileOutputStream(file));
				System.out.println("PdfUA Generation:: path:" + file.getPath() + ", tamaño:" + size);
				logger.debug(("PdfUA Generation:: path:" + file.getPath()) + ", tamaño:" + size);
				Assert.assertTrue("PdfUA Generation:: size bigger than 0 assertion", size > 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} 
	}

	@Test
	public void testConfirmationPageToPDF() {
		try {
			InputStream html = TestUtil.getInputHtml();

			File fileOut = new File(TestUtil.getTestPath() + "testConfirmationPageToPDF.pdf");
			FileOutputStream outPDF = new FileOutputStream(fileOut);

			HTMLToPDFConverter.htmlToPDF(IOUtils.getStringFromInputStream(html), outPDF);
			System.out.println("testConfirmationPageToPDFFlyingJericho:: PDF generado en:" + fileOut.getPath());
			logger.debug(("testConfirmationPageToPDFFlyingJericho:: PDF generado en:" + fileOut.getPath()));
			Assert.assertNotNull("Confirmation PDF Flying Saurce and Jericho with CSS generated not null assertion",
					outPDF);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}
