package com.xcobbler.jhu.mips.sim.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class TestCollection {
  @XmlElement
  private String name;
  @XmlElement
  private String date;
  @XmlElement
  private String circFile;
  @XmlElement(name = "test")
  @XmlElementWrapper(name = "tests")
  private List<TestResult> tests;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getCircFile() {
    return circFile;
  }

  public void setCircFile(String circFile) {
    this.circFile = circFile;
  }
  public List<TestResult> getTests() {
    return tests;
  }
  public void setTests(List<TestResult> tests) {
    this.tests = tests;
  }
}
