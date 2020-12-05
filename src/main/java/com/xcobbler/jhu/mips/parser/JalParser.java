package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;
import com.xcobbler.jhu.mips.common.MipsUtils;
import com.xcobbler.jhu.mips.common.ParseException;

public class JalParser extends CommonJParser {

  @Override
  public String getAddress(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data) {
    Integer j = labels.get(parts.get(1));

    if (j != null) {
      return MipsUtils.decimalToHex26(String.valueOf(j));
    }
    throw new ParseException("only handling labels for now");
  }
}
