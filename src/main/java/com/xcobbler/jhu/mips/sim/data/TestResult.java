package com.xcobbler.jhu.mips.sim.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TestResult {
  @XmlElement(name = "name")
  private String name;
  @XmlElement(name = "success")
  private boolean success;
  @XmlElement(name = "registerCheck")
  @XmlElementWrapper(name = "registerChecks")
  private List<RegisterCheck> registerChecks;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public List<RegisterCheck> getRegisterChecks() {
    return registerChecks;
  }

  public void setRegisterChecks(List<RegisterCheck> registerChecks) {
    this.registerChecks = registerChecks;
  }
}
