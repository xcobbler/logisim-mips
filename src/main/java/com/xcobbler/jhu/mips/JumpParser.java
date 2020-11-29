package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class JumpParser implements Parser {

  @Override
  public String parse(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    return MipsUtils.binariesToHex32(
        MipsUtils.hex6ToBin(getOpCode(parts)),
        MipsUtils.hex26ToBin(getAddress(lineNum, parts, labels, data))
        );
  }

  public String getOpCode(List<String> parts) {
    return "02";
  }

  public String getAddress(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    Integer j = labels.get(parts.get(1));

    if (j != null) {
      return MipsUtils.decimalToHex26(String.valueOf(j));
    }
    throw new ParseException("only handling labels for now");
  }

}
