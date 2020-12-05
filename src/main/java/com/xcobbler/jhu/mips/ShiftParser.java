package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class ShiftParser extends CommonRParser {

  @Override
  public String getRs(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getRt(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return super.getRs(lineNum, parts, instrLabels, data);
  }

  @Override
  public String getShamt(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String s = parts.get(3);
    return MipsUtils.decimalToHex6(s);
  }
}
