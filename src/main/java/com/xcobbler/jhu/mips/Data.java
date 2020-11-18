package com.xcobbler.jhu.mips;

public class Data {
  private int offset;
  private String hex32;

  public Data(int offset, String hex32) {
    this.offset = offset;
    this.hex32 = hex32;
  }

  public int getOffset() {
    return offset;
  }

  public String getHex32() {
    return hex32;
  }
}
