package com.msl.pdfa.pdf.gen;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xhtmlrenderer.util.XRLog;

import com.msl.pdfa.pdf.TestUtil;
import com.msl.pdfier.commons.io.IOUtils;

@SpringBootTest
public class HTMLToPDFConverterTest {

	private static Logger logger = LoggerFactory.getLogger(HTMLToPDFConverterTest.class);

	@Autowired
	HTMLToPDFConverter htmlToPdfConverter;

	public void setUp() {
		System.getProperties().setProperty("xr.util-logging.loggingEnabled", "false");
		XRLog.setLoggingEnabled(true);
	}

	@Test
	public void testReadUrlToPDF() {
		try {
			System.getProperties().setProperty("xr.util-logging.loggingEnabled", "false");
			XRLog.setLoggingEnabled(true);
			URL url = new URL(
					"https://donate.wikimedia.org/w/index.php?title=Special:LandingPage&country=ES&uselang=es&utm_medium=sidebar&utm_source=donate&utm_campaign=C13_es.wikipedia.org");
			File fileOut = new File(TestUtil.getTestPath() + "wiki-donaciones.pdf");
			FileOutputStream outPDF = new FileOutputStream(fileOut);

			int size = htmlToPdfConverter.htmlToPDF(url, outPDF);
			System.out.println("PdfUA Generation:: path:" + fileOut.getPath() + ", tamaño:" + size);
			logger.debug(("PdfUA Generation:: path:" + fileOut.getPath()) + ", tamaño:" + size);
			assertNotNull(outPDF, "PdfUA Generation:: not null assertion");
			assertTrue(size > 0, "PdfUA Generation:: size bigger than 0 assertion");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAll() {
		Set<File> keys = null;
		try {
			Map<File, URL> testWebs = new HashMap<>();
			testWebs.put(new File(TestUtil.getTestPath() + "webaim.pdf"), new URL("https://webaim.org/training/"));
			testWebs.put(new File(TestUtil.getTestPath() + "wikipedia.pdf"),
					new URL("https://es.wikipedia.org/wiki/Wikipedia:Portada"));
//			testWebs.put(new File(TestUtil.getTestPath() + "JAWS.pdf"), new URL("http://www.freedomscientific.com/Downloads/JAWS"));
			keys = testWebs.keySet();
			for (File file : keys) {
				int size = htmlToPdfConverter.htmlToPDF(testWebs.get(file), new FileOutputStream(file));
				System.out.println("PdfUA Generation:: path:" + file.getPath() + ", tamaño:" + size);
				logger.debug(("PdfUA Generation:: path:" + file.getPath()) + ", tamaño:" + size);
				assertTrue(size > 0, "PdfUA Generation:: size bigger than 0 assertion");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConfirmationPageToPDF() {
		try {
			InputStream html = TestUtil.getInputHtml();
			File fileOut = new File(TestUtil.getTestPath() + "testConfirmationPageToPDF.pdf");
			FileOutputStream outPDF = new FileOutputStream(fileOut);
			htmlToPdfConverter.htmlToPDF(IOUtils.getStringFromInputStream(html), outPDF);
			System.out.println("testConfirmationPageToPDFFlyingJericho:: PDF generado en:" + fileOut.getPath());
			logger.debug(("testConfirmationPageToPDFFlyingJericho:: PDF generado en:" + fileOut.getPath()));
			assertNotNull(outPDF, "Confirmation PDF Flying Saurce and Jericho with CSS generated not null assertion");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
