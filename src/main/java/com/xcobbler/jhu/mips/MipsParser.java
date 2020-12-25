package com.xcobbler.jhu.mips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.xcobbler.jhu.mips.common.MipsUtils;
import com.xcobbler.jhu.mips.common.ParseException;
import com.xcobbler.jhu.mips.parser.BranchParser;
import com.xcobbler.jhu.mips.parser.CommonIParser;
import com.xcobbler.jhu.mips.parser.CommonRParser;
import com.xcobbler.jhu.mips.parser.JalParser;
import com.xcobbler.jhu.mips.parser.JumpParser;
import com.xcobbler.jhu.mips.parser.JumpRegisterParser;
import com.xcobbler.jhu.mips.parser.LoadWordParser;
import com.xcobbler.jhu.mips.parser.Parser;
import com.xcobbler.jhu.mips.parser.ShiftParser;
import com.xcobbler.jhu.mips.parser.SyscallParser;

/**
 * 
 * Converts MIPS program into machine code
 * 
 * @author Xavier Coble
 *
 */
public class MipsParser {

  private final Map<String, Parser> PARSERS = new HashMap<String, Parser>();
  private final Parser COMMON_R = new CommonRParser();
  private final Parser SHIFT = new ShiftParser();
  private final Parser COMMON_I = new CommonIParser();

  public MipsParser(boolean exactMips) {
    initParsers(exactMips);
  }

  private void initParsers(boolean exactMips) {
    PARSERS.put("add", COMMON_R);
//    PARSERS.put("addu", COMMON_R);
    PARSERS.put("and", COMMON_R);
    PARSERS.put("jr", new JumpRegisterParser());
    PARSERS.put("nor", COMMON_R);
    PARSERS.put("or", COMMON_R);
    PARSERS.put("slt", COMMON_R);
//    PARSERS.put("sltu", COMMON_R);
    PARSERS.put("sll", SHIFT);
    PARSERS.put("srl", COMMON_R);
    PARSERS.put("sub", COMMON_R);
//    PARSERS.put("subu", COMMON_R);
    PARSERS.put("syscall", new SyscallParser());

    PARSERS.put("addi", COMMON_I);
//    PARSERS.put("addiu", COMMON_I);
    PARSERS.put("andi", COMMON_I);
    PARSERS.put("beq", new BranchParser());
    PARSERS.put("bne", new BranchParser());
//    PARSERS.put("lbu", COMMON_I);
//    PARSERS.put("lhu", COMMON_I);
//    PARSERS.put("ll", COMMON_I);
//    PARSERS.put("lui", COMMON_I);
    PARSERS.put("lw", new LoadWordParser());
    PARSERS.put("ori", COMMON_I);
    PARSERS.put("slti", COMMON_I);
//    PARSERS.put("sltiu", COMMON_I);
//    PARSERS.put("sb", COMMON_I);
//    PARSERS.put("sc", COMMON_I);
//    PARSERS.put("sh", COMMON_I);
    PARSERS.put("sw", new LoadWordParser());

    PARSERS.put("j", new JumpParser(exactMips));
    PARSERS.put("jal", new JalParser(exactMips));
  }

  public MipsParserResult parse(String contents) {
    MipsParserResult ret = new MipsParserResult();

    List<String> lines = Arrays.asList(contents.split("\n")).stream().filter(s -> !s.trim().isEmpty())
        .collect(Collectors.toList());

    // line count - comments, blank lines, labels
    int lineNum = 0;
    boolean parsingText = true;
    List<String> programWords = new ArrayList<String>();
    List<String> dataWords = new ArrayList<String>();

    Map<String, Integer> textLabels = new HashMap<String, Integer>();
    Map<String, Data> data = new LinkedHashMap<String, Data>();

    // get labels and data
    for (String rawLine : lines) {
      String line = rawLine.trim();

      if (line.startsWith("#") || line.isEmpty()) {
        continue;
      }
      List<String> parts = Arrays.asList(line.split(" |,|:")).stream().filter(s -> !s.trim().isEmpty())
          .collect(Collectors.toList());

      if (parts.isEmpty()) {
        throw new ParseException("No parts for line: " + rawLine);
      }
      String first = parts.get(0);
      if (parsingText) {
        if (line.endsWith(":")) {
          textLabels.put(line.substring(0, line.length() - 1), lineNum);
          continue;
        } else if (".text".equals(first)) {
          continue;
        } else if (".data".equals(first)) {
          lineNum = 0;
          parsingText = false;
          continue;
        }
      } else {
        if (parts.size() == 2) {
          data.put(first, new Data(lineNum, MipsUtils.decimalToHex32(parts.get(1))));
        }
      }
      lineNum++;
    }

    lineNum = 0;
    parsingText = true;
    // parse text with labels
    for (String rawLine : lines) {
      String line = rawLine.trim();

      if (line.startsWith("#") || line.isEmpty() || line.endsWith(":")) {
        continue;
      }
      List<String> parts = Arrays.asList(line.split(" |,")).stream().filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());


      if (parts.isEmpty()) {
        throw new ParseException("No parts for line: " + rawLine);
      }
      String first = parts.get(0);
      if (parsingText) {
        if (".text".equals(first)) {
          continue;
        } else if (".data".equals(first)) {
          parsingText = false;
          break;
        } else {
          Parser parser = PARSERS.get(first);
          if (parser != null) {
            String instr = parser.parse(lineNum, parts, textLabels, data);
            programWords.add(instr);
          } else {
            throw new ParseException("There is no parser configured for: " + first);
          }
        }
      }
      lineNum++;
    }
    for (Map.Entry<String, Data> entry : data.entrySet()) {
      dataWords.add(entry.getValue().getHex32());
    }

    MipsProgram prog = new MipsProgram(32, programWords);
    MipsData dat = new MipsData(32, dataWords);

    ret.setProgram(prog);
    ret.setData(dat);

    return ret;
  }

}
