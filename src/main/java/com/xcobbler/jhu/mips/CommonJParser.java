package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class CommonJParser implements Parser {

  @Override
  public String parse(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    return MipsUtils.binariesToHex32(
        MipsUtils.hex6ToBin(getOpCode(lineNum, parts, labels, data)),
        MipsUtils.hex26ToBin(getAddress(lineNum, parts, labels, data)));
  }
  
  public String getOpCode(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    return MipsUtils.getJOpCode(parts.get(0));
  }

  public String getAddress(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    return "0";
  }
}
