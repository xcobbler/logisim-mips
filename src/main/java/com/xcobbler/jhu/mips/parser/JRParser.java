package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;
import com.xcobbler.jhu.mips.common.MipsUtils;

public class JRParser extends CommonRParser {

  @Override
  public String getRs(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    Integer rsNum = MipsUtils.getRegisterNum(parts.get(1));
    return Integer.toString(rsNum, 16);
  }

  @Override
  public String getRt(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getRd(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getShamt(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getFunct(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }
}
