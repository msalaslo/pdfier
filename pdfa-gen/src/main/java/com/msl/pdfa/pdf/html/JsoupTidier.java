package com.msl.pdfa.pdf.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JsoupTidier {
	public static Document parse(String html) {
        Document document = Jsoup.parse(html);
        return document;
    }
	
	public static String getLanguage(Document document) {
		Element link = document.select("html").first();
		return link.attr("lang");
    }
	
	public static String getUTF8String(Document document) {
        document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml); //This will ensure the validity
        document.outputSettings().charset("UTF-8");
        return document.toString();
    }
}
