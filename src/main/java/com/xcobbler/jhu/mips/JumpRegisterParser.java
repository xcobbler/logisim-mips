package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class JumpRegisterParser extends CommonRParser {
  @Override
  public String getRs(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rs = parts.get(1);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rs);
    return Integer.toString(rsNum, 16);
  }

  @Override
  public String getRt(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getRd(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }
}
