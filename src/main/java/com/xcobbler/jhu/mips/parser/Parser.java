package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;

/**
 * Responsible for translating a single MIPS line into machine code instructions
 * 
 * @author Xavier Coble
 */
public interface Parser {
  /**
   * 
   * @param lineNum - the line where the instruction is (starting at 0)
   * @param parts   - the MIPS line split by commas and spaces
   * @param labels  - the lines in which labels point to
   * @param data    - the data section
   * @return the machine code instruction (in hex) - no preceding 0x
   */
  public String parse(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data);
}
