package com.xcobbler.jhu.mips.sim.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterCheck {
  private boolean success;
  private int expected;
  private int actual;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public int getExpected() {
    return expected;
  }

  public void setExpected(int expected) {
    this.expected = expected;
  }

  public int getActual() {
    return actual;
  }

  public void setActual(int actual) {
    this.actual = actual;
  }
}
