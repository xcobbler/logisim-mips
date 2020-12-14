package com.xcobbler.jhu.mips.common;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.xcobbler.jhu.mips.sim.data.RegisterCheck;
import com.xcobbler.jhu.mips.sim.data.SuiteCollection;
import com.xcobbler.jhu.mips.sim.data.TestResult;
import com.xcobbler.jhu.mips.sim.data.TestSuite;

public class DesktopUtils {
  public static void openHtmlDoc() throws IOException, URISyntaxException {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
      File file = new File("test.html");
      Desktop.getDesktop().browse(file.getAbsoluteFile().toURI());
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException, JAXBException {
    TestSuite tc = new TestSuite();
    tc.setDate(new Date().toString());
    tc.setTests(new ArrayList<TestResult>());
    tc.setCircFile("C:/Users/xavie/Google Drive/jhu/01_comp_arch/project/logisim-mips/mips.circ");

    TestResult tr = new TestResult();
    tr.setName("addi");

    RegisterCheck rc = new RegisterCheck();

    rc.setActual(12);
    rc.setExpected(24);

    tr.setRegisterChecks(new ArrayList<RegisterCheck>());

    tr.getRegisterChecks().add(rc);

    tc.getTests().add(tr);
    tc.getTests().add(tr);
    tc.setName("student 1");

    SuiteCollection sc = new SuiteCollection();
    sc.setSuites(new ArrayList<TestSuite>());

    sc.getSuites().add(tc);
    sc.setName("class 1");

    JAXBContext c = JAXBContext.newInstance(SuiteCollection.class);

    Marshaller m = c.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    m.marshal(sc, new File("target/out-" + System.currentTimeMillis() + ".xml"));
//    DesktopUtils.openHtmlDoc();
  }
}
