package com.xcobbler.jhu.mips.sim.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SuiteCollection {
  @XmlElement
  private String name;
  @XmlElement
  private String date;
  @XmlElement(name = "suite")
  @XmlElementWrapper(name = "suites")
  private List<TestSuite> suites;

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

  public List<TestSuite> getSuites() {
    return suites;
  }

  public void setSuites(List<TestSuite> suites) {
    this.suites = suites;
  }
}
