package org.frankframework.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.xml.sax.ContentHandler;

import org.frankframework.configuration.ConfigurationException;
import org.frankframework.stream.Message;
import org.frankframework.util.TransformerPool.OutputType;
import org.frankframework.xml.XmlWriter;

public class TransformerPoolNamespaceUnawarenessTest {

	public static final String NAMESPACED_INPUT_MESSAGE="<root><sub xmlns=\"http://dummy\">+</sub><sub>-</sub></root>";
	public static final String NAMESPACELESS_XPATH="/root/sub";
	public static final String NAMESPACELESS_STYLESHEET="/Util/TransformerPool/NamespacelessStylesheet.xsl";
	public static final String NAMESPACE_INSENSITIVE_RESULT= "+ -";
	public static final String NAMESPACE_COMPLIANT_RESULT= "-";
	public static final String NAMESPACE_INSENSITIVE_FIRST_RESULT= "+";

	public static final String XPATH_0_AND_2_RESULT = NAMESPACE_INSENSITIVE_RESULT;
	public static final String STYLESHEET_AUTO_UNAWARE_RESULT = NAMESPACE_INSENSITIVE_FIRST_RESULT;
	public static final String XSLT_1_UNAWARE_RESULT = NAMESPACE_INSENSITIVE_FIRST_RESULT;

	public TransformerPool getTransformerPool(String xpath, String stylesheet, int xsltVersion) throws ConfigurationException {
		return TransformerPool.configureTransformer0(null, null, xpath, stylesheet, OutputType.TEXT, false, null, xsltVersion);
	}

	public void testNamespaceInsensitiveTransformation(String xpath, String stylesheet, int xsltVersion, boolean namespaceAware, String expectedResult) throws Exception {
		TransformerPool tp = getTransformerPool(xpath, stylesheet, xsltVersion);

		assertEquals(expectedResult, tp.transformToString(NAMESPACED_INPUT_MESSAGE, null, namespaceAware));
		assertEquals(expectedResult, tp.transformToString(new Message(NAMESPACED_INPUT_MESSAGE), null, namespaceAware));

		XmlWriter writer = new XmlWriter();
		writer.setTextMode(true);
		ContentHandler handler = tp.getTransformerFilter(writer, !namespaceAware, false);
		XmlUtils.parseXml(NAMESPACED_INPUT_MESSAGE, handler);

		assertEquals(expectedResult, writer.toString());
	}

	public void testNamespaceInsensitiveStylesheetTransformation(int xsltVersion, boolean namespaceAware, String expectedResult) throws Exception {
		testNamespaceInsensitiveTransformation(null, NAMESPACELESS_STYLESHEET, xsltVersion, namespaceAware, expectedResult);
	}

	public void testNamespaceInsensitiveXPathTransformation(int xsltVersion, boolean namespaceAware, String expectedResult) throws Exception {
		testNamespaceInsensitiveTransformation(NAMESPACELESS_XPATH, null, xsltVersion, namespaceAware, expectedResult);
	}


	@Test
	public void testNamespaceInsensitiveXPathXslt2NamespaceUnaware() throws Exception {
		testNamespaceInsensitiveXPathTransformation(2, false, XPATH_0_AND_2_RESULT);
	}

	@Test
	public void testNamespaceInsensitiveXPathXslt2NamespaceAware() throws Exception {
		testNamespaceInsensitiveXPathTransformation(2, true, XPATH_0_AND_2_RESULT);
	}

	@Test
	public void testNamespaceInsensitiveXPathXsltAutoNamespaceUnaware() throws Exception {
		testNamespaceInsensitiveXPathTransformation(0, false, XPATH_0_AND_2_RESULT);
	}

	@Test
	public void testNamespaceInsensitiveXPathXsltAutoNamespaceAware() throws Exception {
		testNamespaceInsensitiveXPathTransformation(0, true, XPATH_0_AND_2_RESULT);
	}

	@Test
	public void testNamespaceInsensitiveXPathXslt1NamespaceAware() throws Exception {
		testNamespaceInsensitiveXPathTransformation(1, true, NAMESPACE_COMPLIANT_RESULT); // for XSLT 1 and namespaceAware (explicitly) put on, the transformation is not namespace insensitive
	}

	@Test
	public void testNamespaceInsensitiveXPathXslt1NamespaceUnaware() throws Exception {
		testNamespaceInsensitiveXPathTransformation(1, false, XSLT_1_UNAWARE_RESULT); // under XPath 1.0, only the value of the first node in the matched nodeset is returned
	}

	@Test
	public void testNamespaceInsensitiveStyleSheetXslt2AndAutoNamespaceAware() throws Exception {
		testNamespaceInsensitiveStylesheetTransformation(0, true, NAMESPACE_COMPLIANT_RESULT); // if namespaceaware is set, then transformation is expected to follow namespace rules
		testNamespaceInsensitiveStylesheetTransformation(2, true, NAMESPACE_COMPLIANT_RESULT); // if namespaceaware is set, then transformation is expected to follow namespace rules
	}

	@Test
	public void testNamespaceInsensitiveStyleSheetXslt1NamespaceAware() throws Exception {
		testNamespaceInsensitiveStylesheetTransformation(1, true, NAMESPACE_COMPLIANT_RESULT); // for XSLT 1 and namespaceAware (explicitly) put on, the transformation is not namespace insensitive
	}

	@Test
	public void testNamespaceInsensitiveStyleSheetXslt2AndAutoNamespaceUnaware() throws Exception {
		testNamespaceInsensitiveStylesheetTransformation(0, false, STYLESHEET_AUTO_UNAWARE_RESULT); // under XPath 1.0, only the first match is returned
	}

	@Test
	public void testNamespaceInsensitiveStyleSheetXslt1NamespaceUnaware() throws Exception {
		testNamespaceInsensitiveStylesheetTransformation(1, false, XSLT_1_UNAWARE_RESULT); // under XPath 1.0, only the value of the first node in the matched nodeset is returned
	}
}
