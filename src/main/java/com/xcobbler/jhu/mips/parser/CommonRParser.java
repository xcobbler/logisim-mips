package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;
import com.xcobbler.jhu.mips.common.MipsUtils;

public class CommonRParser implements RParser {

  /**
   * 
   * @param parts
   * @return the hex instruction with no "0x" prefix
   */
  @Override
  public String parse(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return MipsUtils.binariesToHex32(
        MipsUtils.hex6ToBin(getOpCode(lineNum, parts, instrLabels, data)),
        MipsUtils.hex5ToBin(getRs(lineNum, parts, instrLabels, data)),
        MipsUtils.hex5ToBin(getRt(lineNum, parts, instrLabels, data)),
        MipsUtils.hex5ToBin(getRd(lineNum, parts, instrLabels, data)),
        MipsUtils.hex5ToBin(getShamt(lineNum, parts, instrLabels, data)),
        MipsUtils.hex6ToBin(getFunct(lineNum, parts, instrLabels, data))
        );
  }

  @Override
  public String getOpCode(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getRs(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rs = parts.get(2);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rs);
    return Integer.toString(rsNum, 16);
  }

  @Override
  public String getRt(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rt = parts.get(3);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rt);
    return Integer.toString(rsNum, 16);
  }

  @Override
  public String getRd(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rd = parts.get(1);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rd);
    return Integer.toString(rsNum, 16);
  }

  @Override
  public String getShamt(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  @Override
  public String getFunct(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return MipsUtils.getFunct(parts.get(0));
  }
}
