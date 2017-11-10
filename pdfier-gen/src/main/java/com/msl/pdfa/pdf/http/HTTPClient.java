package com.msl.pdfa.pdf.http;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HTTPClient {
	public static String readUrlForPdf(URI url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		// httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String ret = "";
		try {
			HttpEntity entity = response.getEntity();
			ret = EntityUtils.toString(entity);
		} finally {
			response.close();
		}
		return ret;
	}
}
