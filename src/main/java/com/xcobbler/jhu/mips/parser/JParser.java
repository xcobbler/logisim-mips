package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;

/**
 * 
 * Parses J formatted instructions
 * 
 * @author Xavier Coble
 *
 */
public interface JParser extends Parser {

  /**
   * 
   * @param lineNum - the line where the instruction is (starting at 0)
   * @param parts   - the MIPS line split by commas and spaces
   * @param labels  - the lines in which labels point to
   * @param data    - the data section
   * 
   * @return the 6-bit binary value of the opCode
   */
  public String getOpCode(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data);

  /**
   * 
   * @param lineNum - the line where the instruction is (starting at 0)
   * @param parts   - the MIPS line split by commas and spaces
   * @param labels  - the lines in which labels point to
   * @param data    - the data section
   * 
   * @return the 26-bit binary value of the address
   */
  public String getAddress(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data);
}
