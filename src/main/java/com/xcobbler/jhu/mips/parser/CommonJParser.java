package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;
import com.xcobbler.jhu.mips.common.MipsUtils;

public class CommonJParser implements JParser {

  @Override
  public String parse(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    return MipsUtils.binariesToHex32(
        MipsUtils.hex6ToBin(getOpCode(lineNum, parts, labels, data)),
        getAddress(lineNum, parts, labels, data));
  }
  
  @Override
  public String getOpCode(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    return MipsUtils.getJOpCode(parts.get(0));
  }

  @Override
  public String getAddress(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    return "00000000000000000000000000";
  }
}
