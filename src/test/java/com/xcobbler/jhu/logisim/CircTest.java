package com.xcobbler.jhu.logisim;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
      test("addi",
          program(
              i("001000", ZERO, T1, "0000000000000001")
              ),
          reg("t1", 1),
          reg("pc", 1)),
      test("addi addi add",
          program(
              i("001000", ZERO, T0, "0000000000000001"),
              i("001000", ZERO, T1, "0000000000000010"),
              r("000000", T0, T1, T2, "00000", "100000")
              ),
          reg("t2", 3),
          reg("pc", 3)),
      test("addi addi and",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001000", ZERO, T1, "0000000000000011"),
              r("000000", T0, T1, T2, "00000", "100100")
              ),
          reg("t2", 3)),
      test("addi addi or",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001000", ZERO, T1, "0000000000000011"),
              r("000000", T0, T1, T2, "00000", "100101")
              ),
          reg("t2", 7)),
      test("addi addi sub",
          program(
              i("001000", ZERO, T0, "0000000000000111"),
              i("001000", ZERO, T1, "0000000000000011"),
              r("000000", T0, T1, T2, "00000", "100010")
              ),
          reg("t2", 4)),
      test("addi addi nor",
          program(
              i("001000", ZERO, T0, "1111111111111100"),
              i("001000", ZERO, T1, "0000000000000100"),
              r("000000", T0, T1, T2, "00000", "100111")
              ),
          reg("t2", 3))
        });
  }
  //@formatter:on

  private static final String circPath = "C:\\Users\\xavie\\Google Drive\\jhu\\01_comp_arch\\project\\mips.circ";

  @Test(timeout = 3000)
  public void test() {
    MipsData data = null;
    if(this.data != null) {
      data = new MipsData(32, this.data);
    }
    CircSimulation sim = new CircSimulation(new File(circPath), new MipsProgram(32, program), data);

    sim.run(new EndReached());

    sim.printRegisters();

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
    int bin = Integer.parseInt(opCode + rs + rt + rd + shamt + funct, 2);
    String hex = Integer.toString(bin, 16);
    while (hex.length() < 8) {
      hex = "0" + hex;
    }
    return hex;
  }

  private static String i(String opCode, String rs, String rt, String i) {
    int bin = Integer.parseInt(opCode + rs + rt + i, 2);
    String hex = Integer.toString(bin, 16);
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

  private static List<String> program(String... word) {
    return Arrays.asList(word);
  }

  private static List<String> data(String... word) {
    return Arrays.asList(word);
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
