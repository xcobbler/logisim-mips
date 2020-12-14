package com.xcobbler.jhu.mips.sim;

/**
 * Verifies a register's value
 * 
 * @author Xavier Coble
 *
 */
public class RegCheck implements Check {
  private String regName;
  private int expectedValue;

  public RegCheck(String regName, int expectedValue) {
    this.regName = regName;
    this.expectedValue = expectedValue;
  }

  @Override
  public String getName() {
    return "RegCheck";
  }

  public String getRegName() {
    return regName;
  }

  public int getExpectedValue() {
    return expectedValue;
  }

}
