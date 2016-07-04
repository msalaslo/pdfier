package com.msl.pdfa.pdf.commons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.htmlparser.jericho.HTMLElementName;

public class Constants {

	public static final String FONT_DIR = "pdf/fonts/";
	public static final String IMAGES_DIR = "pdf/images/";
	public static final String CSS_FILES = "pdf/flysaa-pdf.css";
	// list of HTML elements that will be retained in the final output:
	public static final Set<String> HTML_ELEMENTS_TO_STRIP = new HashSet<String>(Arrays.asList(new String[] { HTMLElementName.SCRIPT, HTMLElementName.META, HTMLElementName.IFRAME}));
}
