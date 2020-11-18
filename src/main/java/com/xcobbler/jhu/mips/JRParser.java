package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class JRParser extends CommonRParser {

  @Override
  public String getRs(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    Integer rsNum = MipsUtils.getRegisterNum(parts.get(1));
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

  @Override
  public String getShamt(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getFunct(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }
}
