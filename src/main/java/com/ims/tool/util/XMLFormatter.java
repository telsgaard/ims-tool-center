package com.ims.tool.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public final class XMLFormatter {

	private static final Logger logger = Logger.getLogger(XMLFormatter.class.getName());

	private static final int INDENT = 5;

	public static String format(String xml) {
		if (xml == null || "".equals(xml.trim())) {
			return "";
		}
		StreamResult result = new StreamResult(new StringWriter());
		TransformerFactory factory = TransformerFactory.newInstance();
		factory.setAttribute("indent-number", INDENT);
		try {
			Source source = new StreamSource(new StringReader(xml));
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(INDENT));
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.transform(source, result);
		} catch (TransformerException e) {
			logger.log(Level.SEVERE, "Problem in formatting XML. : " + e.getMessage());
			return xml;
		}
		String formatXml = result.getWriter().toString();
		formatXml = replaceEmptySessionId(formatXml);
		return formatXml;
	}

	private static String replaceEmptySessionId(String formatXml) {
		formatXml = formatXml.replace("<cai3:sessionId/>", "<cai3:sessionId></cai3:sessionId>");
		formatXml = formatXml.replace("<cai3:SessionId/>", "<cai3:SessionId></cai3:SessionId>");
		return formatXml;
	}
}
