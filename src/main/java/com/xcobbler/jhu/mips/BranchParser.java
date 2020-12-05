package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class BranchParser extends CommonIParser {

  // Branch instructions switch rs and rt
  @Override
  public String getRs(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rs = parts.get(1);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rs);
    return Integer.toString(rsNum, 16);
  }

  // Branch instructions switch rs and rt
  @Override
  public String getRt(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rt = parts.get(2);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rt);
    return Integer.toString(rsNum, 16);
  }

  @Override
  public String getImmediate(long lineNum, List<String> parts, Map<String, Integer> instrLabels,
      Map<String, Data> data) {
    String i = parts.get(3);

    Integer instrLine = instrLabels.get(i);
    if (instrLine != null) {
      return MipsUtils.decimalToHex16(String.valueOf(instrLine - (lineNum + 1)));
    }
    throw new ParseException("only accepting instr labels");

  }
}
