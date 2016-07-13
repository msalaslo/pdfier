package com.msl.pdfa.pdf.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupTidier {
	public static String tidyUp(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml); //This will ensure the validity
        document.outputSettings().charset("UTF-8");
        return document.toString();
    }
}
