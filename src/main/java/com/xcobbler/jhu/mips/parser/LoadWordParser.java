package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;
import com.xcobbler.jhu.mips.common.MipsUtils;
import com.xcobbler.jhu.mips.common.ParseException;

public class LoadWordParser extends CommonIParser {

  @Override
  public String getRs(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    // using $0 as base since our data memory starts at 0
    String i = parts.get(2);
    Data dataVal = data.get(i);
    if (dataVal != null) {
      return "00";
    } else if (i.contains("(") && i.contains(")")) {
      String inner = i.substring(i.indexOf("(") + 1, i.indexOf(")")).trim();
      return Integer.toHexString(MipsUtils.getRegisterNum(inner));
    }
    throw new ParseException("only accepting labels and offset registers");
  }

  @Override
  public String getImmediate(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String i = parts.get(2);
    Data dataVal = data.get(i);
    if (dataVal != null) {
      int offset = dataVal.getOffset();
      return MipsUtils.decimalToHex16(String.valueOf(offset));
    } else if (i.contains("(") && i.contains(")")) {
      String offset = i.substring(0, i.indexOf("(")).trim();
      return MipsUtils.decimalToHex16(offset);
    }
    throw new ParseException("only accepting labels and offset registers");
  }

}
