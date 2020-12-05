package com.xcobbler.jhu.mips.parser;

import java.util.List;
import java.util.Map;

import com.xcobbler.jhu.mips.Data;

/**
 * 
 * Parses I formatted instructions
 * 
 * @author Xavier Coble
 *
 */
public interface IParser extends Parser {

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
   * @return the 5-bit binary value of the rs register number
   */
  public String getRs(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data);

  /**
   * 
   * @param lineNum - the line where the instruction is (starting at 0)
   * @param parts   - the MIPS line split by commas and spaces
   * @param labels  - the lines in which labels point to
   * @param data    - the data section
   * 
   * @return the 5-bit binary value of the rt register number
   */
  public String getRt(long lineNum, List<String> parts, Map<String, Integer> labels, Map<String, Data> data);

  /**
   * 
   * @param lineNum - the line where the instruction is (starting at 0)
   * @param parts   - the MIPS line split by commas and spaces
   * @param labels  - the lines in which labels point to
   * @param data    - the data section
   * 
   * @return the 16-bit binary value of the immediate value
   */
  public String getImmediate(long lineNum, List<String> parts, Map<String, Integer> instrLabels,
      Map<String, Data> data);

}
