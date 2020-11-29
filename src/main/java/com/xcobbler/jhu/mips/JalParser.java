package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class JalParser extends CommonJParser {

  public String getAddress(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    Integer j = labels.get(parts.get(1));

    if (j != null) {
      return MipsUtils.decimalToHex26(String.valueOf(j));
    }
    throw new ParseException("only handling labels for now");
  }
}
