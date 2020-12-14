package com.xcobbler.jhu.mips.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XslUtils {
  private static final TransformerFactory FAC = TransformerFactory.newInstance();

  public static String runXsl(String xslContents, String xml)
      throws TransformerException, UnsupportedEncodingException {
    ByteArrayInputStream xmlIs = new ByteArrayInputStream(xml.getBytes("utf-8"));
    Source xmlInput = new StreamSource(xmlIs);

    ByteArrayInputStream xslIs = new ByteArrayInputStream(xml.getBytes("utf-8"));
    Source xslInput = new StreamSource(xslIs);

    ByteArrayOutputStream boas = new ByteArrayOutputStream();

    Result xmlOutput = new StreamResult(boas);

    Transformer transformer = FAC.newTransformer(xslInput);
    transformer.transform(xmlInput, xmlOutput);

    return boas.toString("utf-8");
  }
}
