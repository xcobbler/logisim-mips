package com.xcobbler.jhu.logisim;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.xcobbler.jhu.mips.MipsData;
import com.xcobbler.jhu.mips.MipsProgram;

@RunWith(Parameterized.class)
public class CircTest {
  //@formatter:off
  @Parameterized.Parameters(name = "{index} - {0}")
  public static Collection<Object[]> testCases() {
    return Arrays.asList(new Object[][] {
      test("add",
          program(
              i("001000", ZERO, T0, "0000000000000001"),
              i("001000", ZERO, T1, "0000000000000010"),
              r("000000", T0, T1, T2, "00000", "100000")
              ),
          reg("t2", 3),
          reg("pc", 3)),
      test("addi",
          program(
              i("001000", ZERO, T1, "0000000000000001")
              ),
          reg("t1", 1),
          reg("pc", 1)),
      test("and",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001000", ZERO, T1, "0000000000000011"),
              r("000000", T0, T1, T2, "00000", "100100")
              ),
          reg("t2", 3)),
      test("andi",
          program(
              i("001000", ZERO, T0, "0000000000000101"),
              i("001100", T0, T1, "0000000000000011")
              ),
          reg("t1", 1)),
      test("beq",
          program(
              i("000100", ZERO, ZERO, "0000 0000 0000 0001"),
              i("001000", T0, T0, "0000000000000010"),
              i("001000", T0, T0, "0000000000000001")
              ),
          reg("t0", 1)),
      test("beqne",
          program(
              i("001000", ZERO, T0, "0000000000000001"),
              i("000101", ZERO, T0, "0000 0000 0000 0001"),
              i("001000", T0, T0, "0000 0000 0000 0011"),
              i("001000", T0, T0, "0000 0000 0000 0001")
              ),
          reg("t0", 2)),
      test("j",
          //remember the jump dest is the word address
          program(
              j("000010", "0000 0000 0000 0000 0000 0000 10"),
              i("001000", T0, T0, "0000 0000 0000 0001"),
              i("001000", T0, T0, "0000 0000 0000 0011")
              ),
          reg("t0", 3)),
//      test("jal TODO",
//          program(
//              j("000010", "0000 0000 0000 0000 0000 0001 00"),
//              i("001000", ZERO, T0, "0000 0000 0000 0001"),
//              i("001000", ZERO, T0, "0000 0000 0000 0011")
//              ),
//          reg("t0", 3)),
      test("jr",
          program(
              i("001000", ZERO, T0, "0000 0000 0000 0011"),
              r("000000", T0, ZERO, ZERO, "00000", "001000"),
              i("001000", T1, T1, "0000 0000 0000 0001"),
              i("001000", T1, T1, "0000 0000 0000 0011")
              ),
          reg("t1", 3)),
//      test("jbu TODO",
//          program(
//              i("001000", ZERO, T0, "0000 0000 0000 1100"),
//              r("000011", T0, ZERO, ZERO, "00000", "000100"),
//              i("001000", ZERO, T1, "0000 0000 0000 0001"),
//              i("001000", ZERO, T1, "0000 0000 0000 0011")
//              ),
//          reg("t1", 3)),
//      test("lhu TODO",
//          program(
//              i("001000", ZERO, T0, "0000 0000 0000 1100"),
//              r("000011", T0, ZERO, ZERO, "00000", "000100"),
//              i("001000", ZERO, T1, "0000 0000 0000 0001"),
//              i("001000", ZERO, T1, "0000 0000 0000 0011")
//              ),
//          reg("t1", 3)),
//      test("ll TODO",
//          program(
//              i("001000", ZERO, T0, "0000 0000 0000 1100"),
//              r("000011", T0, ZERO, ZERO, "00000", "000100"),
//              i("001000", ZERO, T1, "0000 0000 0000 0001"),
//              i("001000", ZERO, T1, "0000 0000 0000 0011")
//              ),
//          reg("t1", 3)),
//      test("lui TODO",
//          program(
//              i("001000", ZERO, T0, "0000 0000 0000 1100"),
//              r("000011", T0, ZERO, ZERO, "00000", "000100"),
//              i("001000", ZERO, T1, "0000 0000 0000 0001"),
//              i("001000", ZERO, T1, "0000 0000 0000 0011")
//              ),
//          reg("t1", 3)),
      test("lw - 0 from 0 offset",
          program(
              i("100011", T0, T1, "0000 0000 0000 0000")
              ),
          data(
              "0000 0000 0000 0000 0000 0000 0000 0100"
              ),
          reg("t1", 4)),
      test("lw - 1 from 1 offset",
          program(
              i("001000", ZERO, T0, "0000 0000 0000 0001"),
              i("100011", T0, T1, "0000 0000 0000 0001")
              ),
          data(
              "0000 0000 0000 0000 0000 0000 0000 0100",
              "0000 0000 0000 0000 0000 0000 0000 1000",
              "0000 0000 0000 0000 0000 0000 0001 0000"
              ),
          reg("t1", 16)),
      test("nor",
          program(
              i("001000", ZERO, T0, "1111111111111100"),
              i("001000", ZERO, T1, "0000000000000100"),
              r("000000", T0, T1, T2, "00000", "100111")
              ),
          reg("t2", 3)),
      test("or",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001000", ZERO, T1, "0000000000000011"),
              r("000000", T0, T1, T2, "00000", "100101")
              ),
          reg("t2", 7)),
      test("ori",
          program(
              i("001000", ZERO, T0, "0000000000000001"),
              i("001101", T0, T1, "0000000000000100")
              ),
          reg("t1", 5)),
      //TODO should test signed neg numbers as well
      test("slt - 1",
          program(
              i("001000", ZERO, T0, "0000000000000011"),
              i("001000", ZERO, T1, "0000000000000111"),
              r("000000", T2, T0, T1, "00000", "101010")
              ),
          reg("t2", 1)),
      test("slt - 0",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001000", ZERO, T1, "0000000000000011"),
              r("000000", T2, T0, T1, "00000", "101010")
              ),
          reg("t2", 0)),
      test("slti - 1",
          program(
              i("001000", ZERO, T0, "0000000000000011"),
              i("001010", T0, T1, "0000000000000111")
              ),
          reg("t1", 1)),
      test("slti - 0",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001010", T0, T1, "0000000000000011")
              ),
          reg("t1", 0)),
      test("sltiu - 1",
          program(
              i("001000", ZERO, T0, "0000000000000011"),
              i("001011", T0, T1, "0000000000000111")
              ),
          reg("t1", 1)),
      test("sltiu - 0",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001011", T0, T1, "0000000000000011")
              ),
          reg("t1", 0)),
      test("sltu - 1",
          program(
              i("001000", ZERO, T0, "0000000000000011"),
              i("001000", ZERO, T1, "0000000000000111"),
              r("000000", T2, T0, T1, "00000", "101011")
              ),
          reg("t2", 1)),
      test("sltu - 0",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001000", ZERO, T1, "0000000000000011"),
              r("000000", T2, T0, T1, "00000", "101011")
              ),
          reg("t2", 0)),
      test("sll",
          program(
              i("001000", ZERO, T0, "0000000000000100"),
              r("000000", ZERO, T0, T2, "00001", "000000")
              ),
          reg("t2", 8)),
      test("srl",
          program(
              i("001000", ZERO, T0, "0000000000000100"),
              r("000000", ZERO, T0, T2, "00001", "000010")
              ),
          reg("t2", 2)),
    //TODO sb
    //TODO sc
    //TODO sh
      test("sw",
          program(
              i("001000", ZERO, T0, "0000000000000100"),
              i("101011", ZERO, T0, "0000000000000000"),
              i("100011", ZERO, T1, "0000 0000 0000 0000")
              ),
          data(
              "0000 0000 0000 0000 0000 0000 0000 0000"
              ),
          reg("t1", 4)),
      test("sub",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001000", ZERO, T1, "0000000000000011"),
              r("000000", T0, T1, T2, "00000", "100010")
              ),
          reg("t2", 4))
        });
  }
  //@formatter:on

  private static final String circPath = "C:\\Users\\xavie\\Google Drive\\jhu\\01_comp_arch\\project\\logisim-mips\\mips.circ";

//  @Test
  @Test(timeout = 3000)
  public void test() {
    System.out.println("\nstart " + this.testName + ":");
    MipsData data = null;
    if(this.data != null) {
      data = new MipsData(32, this.data);
    }

//    long start = System.currentTimeMillis();
//    System.out.println(testName + " before sim: " + new Date() + " " + start % 1000);
    for (String s : program) {
      System.out.println(s);
    }

    CircSimulation sim = new CircSimulation(new File(circPath), new MipsProgram(32, program), data);

    long end = System.currentTimeMillis();
//    System.out.println(testName + " sim = " + (end - start));
//    System.out.println(testName + " after sim: " + new Date() + " " + start % 1000);

    SimResult res = sim.run(new EndReached());

//    long rr = System.currentTimeMillis();
//    System.out.println(testName + " after run: " + new Date() + " " + start % 1000);

//    System.out.println(testName + " run = " + (rr - end));

    System.out.println("end " + this.testName + ": " + res.getCycles());

//    sim.printRegisters();

    String err = "";

    for (Check c : checks) {
      if (c instanceof RegCheck) {
        int actual = sim.getValue(((RegCheck) c).getRegName());
        err += checkValue(((RegCheck) c).getRegName(), ((RegCheck) c).getExpectedValue(), actual);
      } else {
        err += "\nUnimplemented check: " + c.getClass().getSimpleName();
      }
    }

    if (!err.isEmpty()) {
      Assert.fail(err);
    }
  }

  private static String checkValue(String context, int expected, int actual) {
    String ret = "";

    if (expected != actual) {
      ret += "\nFor " + context + ", Expected value to be [" + expected + "], but got [" + actual + "]";
    }

    return ret;
  }

//  public static long getUnsignedInt(int x) {
//    return x & (-1L >>> 32);
//  }

  private static RegCheck reg(String name, int value) {
    return new RegCheck(name, value);
  }

  // hex return
  private static String r(String opCode, String rs, String rt, String rd, String shamt, String funct) {
    long bin = Long.parseLong((opCode + rs + rt + rd + shamt + funct).replace(" ", ""), 2);
    String hex = Long.toString(bin, 16);
    while (hex.length() < 8) {
      hex = "0" + hex;
    }
    return hex;
  }

  private static String bin2hex(String binStr) {
    long bin = Long.parseLong(binStr.replace(" ", ""), 2);
    String hex = Long.toString(bin, 16);
    while (hex.length() < 8) {
      hex = "0" + hex;
    }
    return hex;
  }

  private static String i(String opCode, String rs, String rt, String i) {
    long bin = Long.parseLong((opCode + rs + rt + i).replace(" ", ""), 2);
    String hex = Long.toString(bin, 16);
    while (hex.length() < 8) {
      hex = "0" + hex;
    }
    return hex;
  }
  
  private static String j(String opCode, String address) {
    long bin = Long.parseLong((opCode + address).replace(" ", ""), 2);
    String hex = Long.toString(bin, 16);
    while (hex.length() < 8) {
      hex = "0" + hex;
    }
    return hex;
  }

  private static Object[] test(String testName, List<String> program, Check check, Check... checks) {
    return test(testName, program, null, check, checks);
  }

  private static Object[] test(String testName, List<String> program, List<String> data, Check check, Check... checks) {
    List<Check> list = new ArrayList<Check>();
    list.add(check);
    list.addAll(Arrays.asList(checks));
    return new Object[] { testName, program, data, list };
  }

  private static List<String> list(String... word) {
    return Arrays.asList(word);
  }

  /**
   * @param word in hex format
   */
  private static List<String> program(String... word) {
    return Arrays.asList(word);
  }

  /**
   * 
   * @param word in binary
   */
  private static List<String> data(String... word) {
    List<String> ret = new ArrayList<String>(Arrays.asList(word));

    return ret.stream().map(s -> bin2hex(s)).collect(Collectors.toList());
  }

  private String testName;
  private List<String> program;
  private List<String> data;
  private List<Check> checks;

  public CircTest(String testName, List<String> program, List<String> data, List<Check> checks) {
    this.testName = testName;
    this.program = program;
    this.data = data;
    this.checks = checks;
  }
  
  private static final String ZERO = "00000";
  private static final String T0 = "01000";
  private static final String T1 = "01001";
  private static final String T2 = "01010";
}
