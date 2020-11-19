package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public class LoadWordParser extends CommonIParser {
  @Override
  public String getRs(List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    // using $0 as base since our data memory starts at 0
    return "00";
  }

  @Override
  public String getImmediate(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String i = parts.get(2);
    Data dataVal = data.get(i);
    if (dataVal != null) {
      int offset = dataVal.getOffset();
      return MipsUtils.decimalToHex16(String.valueOf(offset));
    }
    throw new ParseException("only accepting labels");
  }

}
