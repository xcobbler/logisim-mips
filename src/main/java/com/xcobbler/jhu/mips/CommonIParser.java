package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class CommonIParser implements Parser {

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
        MipsUtils.hex16ToBin(getImmediate(parts, instrLabels, data))
        );
  }

  public String getOpCode(List<String> parts) {
    return MipsUtils.getIOpCode(parts.get(0));
  }

  public String getRs(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rs = parts.get(2);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rs);
    return Integer.toString(rsNum, 16);
  }

  public String getRt(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String rt = parts.get(1);
    // convert rs, rt, ect to register number
    Integer rsNum = MipsUtils.getRegisterNum(rt);
    return Integer.toString(rsNum, 16);
  }

  public String getImmediate(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String i = parts.get(3);
    return Long.toHexString(Long.parseLong(i, 10));
  }
}
