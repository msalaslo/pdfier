package com.msl.pdfa.pdf.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msl.pdfa.pdf.exception.UtilException;

public class IOUtils {
	
	private static Logger logger = LoggerFactory.getLogger(IOUtils.class);

	public static File stringToFile(String content, File file) throws UtilException{		
		try {
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			logger.error("Error parsing file.", e);
			throw new UtilException("Error parsing file.", e);
		}		
		return file;
	}
	
	public static File inputStreamToFile(InputStream is, File file) throws UtilException{
		OutputStream outputStream = null;
		try {			
			// write the inputStream to a FileOutputStream
			outputStream = new FileOutputStream(file);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = is.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}		
		} catch (IOException e) {
			logger.error("Error piping inputstream to outputstream.", e);
			throw new UtilException("Error piping inputstream to outputstream.", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("Error closing inputstream.", e);
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					logger.error("Error closing outputstream.", e);
				}
			}
		}
		return file;
	}
	
	public static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			logger.error("Error reading inputstream.", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("Error closing buffered reader.", e);
				}
			}
		}
		return sb.toString();
	}	
	
	public static InputStream getInputStream(String inString) throws IOException{
		// convert String into InputStream
		InputStream is = new ByteArrayInputStream(inString.getBytes());
		return is;
	}
}
