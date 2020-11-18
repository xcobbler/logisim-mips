package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class ShiftParser extends CommonRParser {
  @Override
  public String getRs(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getRt(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return super.getRs(parts, instrLabels, data);
  }

  @Override
  public String getShamt(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String s = parts.get(3);
    return MipsUtils.decimalToHex6(s);
  }
}
