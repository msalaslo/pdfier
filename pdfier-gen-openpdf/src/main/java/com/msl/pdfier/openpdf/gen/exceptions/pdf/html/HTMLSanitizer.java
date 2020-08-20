package com.msl.pdfier.openpdf.gen.exceptions.pdf.html;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.EndTag;
import net.htmlparser.jericho.EndTagType;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.HTMLElements;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StartTagType;
import net.htmlparser.jericho.Tag;

/**
 * Provides facilities to sanitize HTML containing unwanted or invalid tags into
 * clean HTML.
 */
public class HTMLSanitizer {
	private HTMLSanitizer() {
	} // not instantiable

	// list of HTML attributes that will be retained in the final output:
	private static final Set<String> NON_VALID_ATTRIBUTE_NAMES = new HashSet<String>(
			Arrays.asList(new String[] { "de_momento_ninguno" }));

	private static final Object VALID_MARKER = new Object();

	/**
	 * Returns a sanitized version of the specified HTML, encoding any unwanted
	 * tags.
	 * 
	 * @param pseudoHTML The potentially invalid HTML to sanitize.
	 * @return a sanitized version of the specified HTML, encoding any unwanted
	 *         tags.
	 */
	public static String encodeInvalidMarkup(String pseudoHTML) {
		return encodeInvalidMarkup(pseudoHTML, false);
	}

	/**
	 * Returns a sanitized version of the specified HTML, encoding any unwanted
	 * tags.
	 * <p>
	 * Encoding unwanted and invalid tags results in them appearing verbatim in the
	 * rendered output, helping to highlight the problem so that the source HTML can
	 * be fixed.
	 * <p>
	 * Specifying a value of <code>true</code> as an argument to the
	 * <code>formatWhiteSpace</code> parameter results in the formatting of white
	 * space as described in the sanitization process in the class description
	 * above.
	 * 
	 * @param pseudoHTML       The potentially invalid HTML to sanitize.
	 * @param formatWhiteSpace Specifies whether white space should be marked up in
	 *                         the output.
	 * @return a sanitized version of the specified HTML, encoding any unwanted
	 *         tags.
	 */
	public static String encodeInvalidMarkup(String pseudoHTML, boolean formatWhiteSpace) {
		return sanitize(pseudoHTML, formatWhiteSpace, false, null);
	}

	/**
	 * Returns a sanitized version of the specified HTML, stripping any unwanted
	 * tags.
	 * 
	 * @param pseudoHTML The potentially invalid HTML to sanitize.
	 * @return a sanitized version of the specified HTML, stripping any unwanted
	 *         tags.
	 */
	public static String stripInvalidMarkup(String pseudoHTML, Set<String> elementsToStrip) {
		return sanitize(pseudoHTML, false, true, elementsToStrip);
	}

	/**
	 * Returns a sanitized version of the specified HTML, stripping any unwanted
	 * tags.
	 * 
	 * @param pseudoHTML       The potentially invalid HTML to sanitize.
	 * @param formatWhiteSpace Specifies whether white space should be marked up in
	 *                         the output.
	 * @return a sanitized version of the specified HTML, stripping any unwanted
	 *         tags.
	 */
	public static String stripInvalidMarkup(String pseudoHTML, boolean formatWhiteSpace) {
		return sanitize(pseudoHTML, formatWhiteSpace, true, null);
	}

	private static String sanitize(String pseudoHTML, boolean formatWhiteSpace, boolean stripInvalidElements,
			Set<String> elementsToStrip) {
		Source source = new Source(pseudoHTML);
		source.fullSequentialParse();
		OutputDocument outputDocument = new OutputDocument(source);
		List<Tag> tags = source.getAllTags();
		int pos = 0;
		for (Tag tag : tags) {
			if (tag.getBegin() < pos)
				continue; // this might happen if a script element was
							// encountered
			if (processTag(tag, outputDocument, elementsToStrip)) {
				tag.setUserData(VALID_MARKER);
			} else {
				if (!stripInvalidElements)
					continue; // element will be encoded along with surrounding
								// text
				if (tag.getName() == HTMLElementName.SCRIPT && tag.getTagType() == StartTagType.NORMAL) {
					EndTag endTag = tag.getElement().getEndTag();
					if (endTag == null) {
						// script has no end tag, remove everything to the end
						// of the source and don't process any more tags
						outputDocument.remove(tag.getBegin(), source.getEnd());
						return outputDocument.toString();
					}
					// remove the whole script element including tags and
					// content
					outputDocument.remove(tag.getBegin(), endTag.getEnd());
					pos = endTag.getEnd();
					continue;
				}
				outputDocument.remove(tag);
			}
			reencodeTextSegment(source, outputDocument, pos, tag.getBegin(), formatWhiteSpace);
			pos = tag.getEnd();
		}
		reencodeTextSegment(source, outputDocument, pos, source.getEnd(), formatWhiteSpace);
		return outputDocument.toString();
	}

	private static boolean processTag(Tag tag, OutputDocument outputDocument, Set<String> elementsToStrip) {
		String elementName = tag.getName();
		if (elementsToStrip != null && elementsToStrip.contains(elementName))
			return false;
		if (tag.getTagType() == StartTagType.NORMAL) {
			Element element = tag.getElement();
			if (HTMLElements.getEndTagRequiredElementNames().contains(elementName)) {
				if (element.getEndTag() == null)
					return false; // reject start tag if its required end tag is
									// missing
			} else if (HTMLElements.getEndTagOptionalElementNames().contains(elementName)) {
				if (elementName == HTMLElementName.LI && !isValidLITag(tag))
					return false; // reject invalid LI tags
				if (element.getEndTag() == null)
					// insert optional end tag if it is missing
					outputDocument.insert(element.getEnd(), getEndTagHTML(elementName));
			}
			outputDocument.replace(tag, getStartTagHTML(element.getStartTag()));
		} else if (tag.getTagType() == EndTagType.NORMAL) {
			if (tag.getElement() == null)
				return false; // reject end tags that aren't associated with a
								// start tag
			if (elementName == HTMLElementName.LI && !isValidLITag(tag))
				return false; // reject invalid LI tags
			outputDocument.replace(tag, getEndTagHTML(elementName));
		} else {
			return false; // reject abnormal tags
		}
		return true;
	}

	private static boolean isValidLITag(Tag tag) {
		Element parentElement = tag.getElement().getParentElement();
		if (parentElement == null)
			return false; // ignore LI elements without a parent
		if (parentElement.getStartTag().getUserData() != VALID_MARKER)
			return false; // ignore LI elements who's parent is not valid
		// only accept LI tags who's immediate parent is UL or OL.
		return parentElement.getName() == HTMLElementName.UL || parentElement.getName() == HTMLElementName.OL;
	}

	private static void reencodeTextSegment(Source source, OutputDocument outputDocument, int begin, int end,
			boolean formatWhiteSpace) {
		if (begin >= end)
			return;
		Segment textSegment = new Segment(source, begin, end);
		String decodedText = CharacterReference.decode(textSegment);
		String encodedText = formatWhiteSpace ? CharacterReference.encodeWithWhiteSpaceFormatting(decodedText)
				: CharacterReference.encode(decodedText);
		outputDocument.replace(textSegment, encodedText);
	}

	private static CharSequence getStartTagHTML(StartTag startTag) {
		// tidies and filters out non-approved attributes
		StringBuilder sb = new StringBuilder();
		sb.append('<').append(startTag.getName());
		for (Attribute attribute : startTag.getAttributes()) {
			if (!NON_VALID_ATTRIBUTE_NAMES.contains(attribute.getKey())) {
				sb.append(' ').append(attribute.getName());
				if (attribute.getValue() != null) {
					sb.append("=\"");
					sb.append(CharacterReference.encode(attribute.getValue()));
					sb.append('"');
				}
			}
		}
		if (startTag.getElement().getEndTag() == null
				&& !HTMLElements.getEndTagOptionalElementNames().contains(startTag.getName()))
			sb.append(" /");
		sb.append('>');
		return sb;
	}

	private static String getEndTagHTML(String tagName) {
		return "</" + tagName + '>';
	}

}