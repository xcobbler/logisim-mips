package com.xcobbler.jhu.mips.common;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.xcobbler.jhu.mips.sim.data.RegisterCheck;
import com.xcobbler.jhu.mips.sim.data.TestResult;

public class DesktopUtils {
  public static void openHtmlDoc() throws IOException, URISyntaxException {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
      File file = new File("test.html");
      Desktop.getDesktop().browse(file.getAbsoluteFile().toURI());
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException, JAXBException {
    TestResult tr = new TestResult();

    RegisterCheck rc = new RegisterCheck();

    rc.setActual(12);
    rc.setExpected(24);

    tr.setRegisterChecks(new ArrayList());

    tr.getRegisterChecks().add(rc);

    JAXBContext c = JAXBContext.newInstance(TestResult.class);

    Marshaller m = c.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    m.marshal(tr, new File("target/out-" + System.currentTimeMillis() + ".xml"));
//    DesktopUtils.openHtmlDoc();
  }
}
