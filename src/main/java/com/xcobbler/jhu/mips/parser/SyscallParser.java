package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;

public class SyscallParser extends CommonRParser {
  @Override
  public String getRs(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getRt(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getRd(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }
}
