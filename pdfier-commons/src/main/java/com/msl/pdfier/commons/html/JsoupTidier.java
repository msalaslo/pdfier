package com.msl.pdfier.commons.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	
	/**
	 * Adds needed name spaces to the html tag
	 * Useful to avoid error: "he prefix xlink for attribute xlink:href associated with an element ... is not bound"
	 * @param document modified
	 */
	public static void addHTMLNameSpaces(Document document) {
		Elements htmlElements = document.getElementsByTag("html");
		if(!htmlElements.isEmpty()){
			Element htmlElement = htmlElements.get(0);
			htmlElement.attr("xmlns", "http://www.w3.org/2000/svg");
			htmlElement.attr("xmlns:xlink", "http://www.w3.org/1999/xlink");
		}
	}
	
	public static void addLandScapeOrientation(Document document) {
		Elements htmlElements = document.getElementsByTag("head");
		if(!htmlElements.isEmpty()){
			Element htmlElement = htmlElements.get(0);
			htmlElement.append("<style>@page { size: A4 landscape;}</style>");
		}
	}
}
