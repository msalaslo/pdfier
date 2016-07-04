package com.msl.pdfa.pdf.gen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;

import com.msl.pdfa.pdf.commons.Constants;

class ResourceLoaderUserAgent extends ITextUserAgent {

	private static Logger logger = LoggerFactory.getLogger(ResourceLoaderUserAgent.class);

	public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
		super(outputDevice);
	}

	protected InputStream resolveAndOpenStream(String uri) {
		InputStream is = null;
		if (uri != null) {
			Map<String, String> fileNameAndExtension = getFileNameAndExtension(uri);
			String fileName = fileNameAndExtension.keySet().toArray(new String[0])[0];
			String extension = fileNameAndExtension.get(fileName);
			if (is == null && !uri.startsWith("http")) {
				is = resolveInternalStream(uri);
			}
			if (is == null && !uri.startsWith("http")) {
				// Try to find the resource on the classpath
				try {
					// Itext Valid fonts extensions
					if (extension.equalsIgnoreCase(".ttf") || extension.equalsIgnoreCase(".otf")) {
						is = ResourceLoaderUserAgent.class.getResourceAsStream("/" + Constants.FONT_DIR + fileName);
					} else {
						is = ResourceLoaderUserAgent.class.getResourceAsStream("/" + Constants.IMAGES_DIR + fileName);
					}
				} catch (Exception e) {
					logger.info("Error opening stream from classpath with uri" + uri, e);
				}
			}
			if (is == null) {
				HttpURLConnection connection = null;
				// URL proxyUrl = null;
				try {
					// Proxy proxy = new Proxy(Proxy.Type.HTTP, new
					// InetSocketAddress("localhost", 3128));
					// proxyUrl = new URL(uri);
					// connection = proxyUrl.openConnection(proxy);
					connection = (HttpURLConnection) new URL(uri).openConnection();
					connection.connect();

				} catch (Exception e) {
					logger.info("Error opening URL stream from classpath with uri" + uri, e);
				}

				try {
					is = connection.getInputStream();
				} catch (java.net.MalformedURLException e) {
					logger.info("bad URL given: " + uri, e);
				} catch (java.io.FileNotFoundException e) {
					logger.info("item at URI " + uri + " not found");
				} catch (java.io.IOException e) {
					logger.info("IO problem for " + uri, e);
				}
			}
		}
		return is;
	}

	private Map<String, String> getFileNameAndExtension(String uri) {
		String fileName = "";
		String extension = "";
		try {
			int posFileName = uri.lastIndexOf("/");
			if (posFileName != -1) {
				fileName = uri.substring(posFileName + 1, uri.length());
				int pos = fileName.indexOf(".");
				if (pos != -1) {
					extension = fileName.substring(pos, fileName.length());
				}
			}
		} catch (Exception e) {
			logger.debug("Error calculating filename and extension from uri:" + uri, e);
		}
		Map<String, String> ret = new HashMap<String, String>();
		ret.put(fileName, extension);
		return ret;
	}

	private InputStream resolveInternalStream(String uri) {
		InputStream ret = null;
		try {
			ret = new FileInputStream(new File(uri));
		} catch (FileNotFoundException e) {
			logger.info("Error opening internal stream with uri" + uri);
		}
		return ret;
	}
}