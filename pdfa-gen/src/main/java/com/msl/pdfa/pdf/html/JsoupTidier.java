package com.msl.pdfa.pdf.html;

import org.jsoup.Jsoup;

public class JsoupTidier {
	public static String tidyUp(String html) {
        String xhtml = Jsoup.parse(html, "UTF-8").html();
        return xhtml;
    }
}
