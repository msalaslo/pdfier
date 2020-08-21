package com.msl.pdfier.openpdf.gen.pdf.gen;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.openhtmltopdf.extend.FSStream;
import com.openhtmltopdf.extend.HttpStreamFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpStreamFactory implements HttpStreamFactory {

	private final OkHttpClient client = new OkHttpClient();

	@Override
	public FSStream getUrl(String url) {
		Request request = new Request.Builder().url(url).build();

		try {
			final Response response = client.newCall(request).execute();

			return new FSStream() {
				@Override
				public InputStream getStream() {
					return response.body().byteStream();
				}

				@Override
				public Reader getReader() {
					return response.body().charStream();
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}