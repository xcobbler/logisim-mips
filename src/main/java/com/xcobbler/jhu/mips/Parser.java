package com.xcobbler.jhu.mips;

import java.util.List;
import java.util.Map;

public interface Parser {
  public String parse(List<String> parts, Map<String, Integer> labels, Map<String, Data> data);
}
