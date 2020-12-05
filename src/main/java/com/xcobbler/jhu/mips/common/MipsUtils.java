package com.xcobbler.jhu.mips.common;

import java.util.HashMap;
import java.util.Map;

public class MipsUtils {
  private static final Map<String, Integer> REGISTERS = new HashMap<String, Integer>();
  private static final Map<String, String> R_FUNCTIONS = new HashMap<String, String>();
  private static final Map<String, String> I_OPCODE = new HashMap<String, String>();
  private static final Map<String, String> J_OPCODE = new HashMap<String, String>();

  static {
    REGISTERS.put("$zero", 0);
    REGISTERS.put("$at", 1);
    REGISTERS.put("$v0", 2);
    REGISTERS.put("$v1", 3);
    REGISTERS.put("$a0", 4);
    REGISTERS.put("$a1", 5);
    REGISTERS.put("$a2", 6);
    REGISTERS.put("$a3", 7);
    REGISTERS.put("$t0", 8);
    REGISTERS.put("$t1", 9);
    REGISTERS.put("$t2", 10);
    REGISTERS.put("$t3", 11);
    REGISTERS.put("$t4", 12);
    REGISTERS.put("$t5", 13);
    REGISTERS.put("$t6", 14);
    REGISTERS.put("$t7", 15);
    REGISTERS.put("$s0", 16);
    REGISTERS.put("$s1", 17);
    REGISTERS.put("$s2", 18);
    REGISTERS.put("$s3", 19);
    REGISTERS.put("$s4", 20);
    REGISTERS.put("$s5", 21);
    REGISTERS.put("$s6", 22);
    REGISTERS.put("$s7", 23);
    REGISTERS.put("$t8", 24);
    REGISTERS.put("$t9", 25);
    REGISTERS.put("$k0", 26);
    REGISTERS.put("$k1", 27);
    REGISTERS.put("$gp", 28);
    REGISTERS.put("$sp", 29);
    REGISTERS.put("$fp", 30);
    REGISTERS.put("$ra", 31);
  }

  static {
    R_FUNCTIONS.put("add", "20");
    R_FUNCTIONS.put("addu", "21");
    R_FUNCTIONS.put("and", "24");
    R_FUNCTIONS.put("jr", "08");
    R_FUNCTIONS.put("nor", "27");
    R_FUNCTIONS.put("or", "25");
    R_FUNCTIONS.put("slt", "2a");
//    R_FUNCTIONS.put("sltu", "2b");
    R_FUNCTIONS.put("sll", "00");
    R_FUNCTIONS.put("srl", "02");
    R_FUNCTIONS.put("sub", "22");
    R_FUNCTIONS.put("subu", "23");
  }

  static {
    I_OPCODE.put("addi", "08");
    I_OPCODE.put("addiu", "09");
    I_OPCODE.put("andi", "0c");
    I_OPCODE.put("beq", "04");
    I_OPCODE.put("bne", "05");
//    I_OPCODE.put("lbu", "24");
//    I_OPCODE.put("lhu", "25");
//    I_OPCODE.put("ll", "30");
//    I_OPCODE.put("lui", "0f");
    I_OPCODE.put("lw", "23");
    I_OPCODE.put("ori", "0d");
    I_OPCODE.put("slti", "0a");
//    I_OPCODE.put("sltiu", "0b");
//    I_OPCODE.put("sb", "28");
//    I_OPCODE.put("sc", "38");
//    I_OPCODE.put("sh", "29");
    I_OPCODE.put("sw", "2b");
  }

  static {
    J_OPCODE.put("j", "02");
    J_OPCODE.put("jal", "03");
    J_OPCODE.put("jr", "08");
  }

  /**
   * 
   * @return register number
   */
  public static int getRegisterNum(String name) {
    Integer num = REGISTERS.get(name);
    if (num != null) {
      return num;
    } else {
      throw new ParseException("unknown register: " + name);
    }
  }

  public static boolean isR(String rInstr) {
    return R_FUNCTIONS.containsKey(rInstr);
  }

  public static String getFunct(String rInstr) {
    String funct = R_FUNCTIONS.get(rInstr);
    if (funct != null) {
      return funct;
    } else {
      throw new ParseException("unknown instr: " + rInstr);
    }
  }

  public static boolean isI(String iInstr) {
    return I_OPCODE.containsKey(iInstr);
  }

  /**
   * 
   * @return hex value
   */
  public static String getIOpCode(String iInstr) {
    String code = I_OPCODE.get(iInstr);
    if (code != null) {
      return code;
    } else {
      throw new ParseException("unknown instr: " + iInstr);
    }
  }

  public static boolean isJ(String jInstr) {
    return J_OPCODE.containsKey(jInstr);
  }

  /**
   * 
   * @return hex value
   */
  public static String getJOpCode(String jInstr) {
    String code = J_OPCODE.get(jInstr);
    if (code != null) {
      return code;
    } else {
      throw new ParseException("unknown instr: " + jInstr);
    }
  }

  public static String hex6ToBin(String hex6) {
    String ret = Long.toBinaryString(Long.parseLong(hex6, 16));
    while (ret.length() < 6) {
      ret = "0" + ret;
    }
    return ret;
  }

  public static String hex5ToBin(String hex5) {
    String ret = Long.toBinaryString(Long.parseLong(hex5, 16));
    while (ret.length() < 5) {
      ret = "0" + ret;
    }
    return ret;
  }

  public static String hex16ToBin(String hex16) {
    if (hex16.length() > 4) {
      hex16 = hex16.substring(hex16.length() - 4, hex16.length());
    }
    String ret = Long.toBinaryString(Long.parseLong(hex16, 16));
    while (ret.length() < 16) {
      ret = "0" + ret;
    }
    return ret;
  }

  public static String hex26ToBin(String hex26) {
    if (hex26.length() > 7) {
      hex26 = hex26.substring(hex26.length() - 4, hex26.length());
    }
    String ret = Long.toBinaryString(Long.parseLong(hex26, 16));
    while (ret.length() < 26) {
      ret = "0" + ret;
    }
    return ret;
  }

  public static String binariesToHex32(String... parts) {
    String ret = "";
    for (String part : parts) {
      ret += part;
    }
    ret = Long.toHexString(Long.valueOf(ret, 2));
    while (ret.length() < 8) {
      ret = "0" + ret;
    }
    return ret;
  }

  public static String decimalToHex32(String decimal) {
    String ret = Long.toHexString(Long.valueOf(decimal, 10));
    while (ret.length() < 8) {
      ret = "0" + ret;
    }
    return ret;
  }

  public static String decimalToHex26(String decimal) {
    String ret = Long.toHexString(Long.valueOf(decimal, 10));
    while (ret.length() < 7) {
      ret = "0" + ret;
    }
    return ret;
  }

  public static String decimalToHex16(String decimal) {
    String ret = Long.toHexString(Long.valueOf(decimal, 10));
    while (ret.length() < 4) {
      ret = "0" + ret;
    }
    return ret;
  }

  public static String decimalToHex6(String decimal) {
    String ret = Long.toHexString(Long.valueOf(decimal, 10));
    while (ret.length() < 2) {
      ret = "0" + ret;
    }
    return ret;
  }

}
