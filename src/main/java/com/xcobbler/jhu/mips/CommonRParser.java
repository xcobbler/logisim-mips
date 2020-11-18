package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class CommonRParser implements Parser {
//  private String funct;
//  /**
//   * 
//   * @param funct - 6 bit hex
//   */
//  public CommonRParser(String funct) {
//    this.funct = funct;
//  }

  /**
   * 
   * @param parts
   * @return the hex instruction with no "0x" prefix
   */
  public String parse(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return MipsUtils.binariesToHex32(
        MipsUtils.hex6ToBin(getOpCode(parts)),
        MipsUtils.hex5ToBin(getRs(parts, instrLabels, data)),
        MipsUtils.hex5ToBin(getRt(parts, instrLabels, data)),
        MipsUtils.hex5ToBin(getRd(parts, instrLabels, data)),
        MipsUtils.hex5ToBin(getShamt(parts, instrLabels, data)),
        MipsUtils.hex6ToBin(getFunct(parts, instrLabels, data))
        );
  }

  public String getOpCode(List<String> parts) {
    return "00";
  }

  public String getRs(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rs = parts.get(2);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rs);
    return Integer.toString(rsNum, 16);
  }

  public String getRt(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rt = parts.get(3);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rt);
    return Integer.toString(rsNum, 16);
  }

  public String getRd(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rd = parts.get(1);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rd);
    return Integer.toString(rsNum, 16);
  }

  public String getShamt(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return "00";
  }

  public String getFunct(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    return MipsUtils.getFunct(parts.get(0));
  }
}
