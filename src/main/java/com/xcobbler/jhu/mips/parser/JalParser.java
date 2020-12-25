package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;
import com.xcobbler.jhu.mips.common.MipsUtils;
import com.xcobbler.jhu.mips.common.ParseException;

public class JalParser extends CommonJParser {

  private static final int TEXT_START = 1048576;
  private boolean exactMips;

  public JalParser(boolean exactMips) {
    this.exactMips = exactMips;
  }

  @Override
  public String getAddress(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    Integer j = labels.get(parts.get(1));

    if (j != null) {
      if (exactMips) {
//        j *= 4;
        j += TEXT_START;
      }
      return MipsUtils.decimalToBin26(String.valueOf(j));
    }
    throw new ParseException("only handling labels for now");
  }
}
