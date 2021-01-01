package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;
import com.xcobbler.jhu.mips.common.MipsUtils;
import com.xcobbler.jhu.mips.common.ParseException;

public class MipsLoadWordParser extends CommonIParser {

  @Override
  public String parse(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    // for exactMips, this results in:
    // 1) a load upper immediate of the address, but shifted to the right by 16 bits into $1
    // 2) then a load word on the value of $1 with the offset being the lower 16 bits ($1 just has the upper part)
    // using $0 as base since our data memory starts at 0
    // do lui - 001111 - 00000 - 00001 - 0x1001 (constant) 16 high bits of address
    // do lw - 100011 - 00001 - 01000 - 16 bit lower bits of address

    return "3c011001\n" + MipsUtils.binariesToHex32(
        MipsUtils.hex6ToBin(getOpCode(lineNum, parts, instrLabels, data)),
        "00001",
        MipsUtils.hex5ToBin(getRt(lineNum, parts, instrLabels, data)),
        MipsUtils.hex16ToBin(getImmediate(lineNum, parts, instrLabels, data)));
  }

  @Override
  public String getRs(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    throw new ParseException("should not get here");
  }

  @Override
  public String getImmediate(long lineNum, List<String> parts, Map<String, Integer> instrLabels, Map<String, Data> data) {
    String i = parts.get(2);
    Data dataVal = data.get(i);
    if (dataVal != null) {
      int offset = dataVal.getOffset();
      return MipsUtils.decimalToHex16(String.valueOf(offset * 4));
    } else if (i.contains("(") && i.contains(")")) {
      String offset = i.substring(0, i.indexOf("(")).trim();
      return MipsUtils.decimalToHex16(offset);
    }
    throw new ParseException("only accepting labels and offset registers");
  }

}
