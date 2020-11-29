package com.xcobbler.jhu.mips;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.xcobbler.jhu.logisim.Check;
import com.xcobbler.jhu.logisim.CircSimulation;
import com.xcobbler.jhu.logisim.EndReached;
import com.xcobbler.jhu.logisim.RegCheck;

@RunWith(Parameterized.class)
public class MipsRegressionTest {

  @SuppressWarnings("unused")
  private String textName = null;
  private String asmContents = null;
  private List<Check> checks = null;

  public MipsRegressionTest(String testName, String asmContents, List<Check> checks) {
    this.textName = testName;
    this.asmContents = asmContents;
    this.checks = checks;
  }

  @Parameterized.Parameters(name = "{index} - {0}")
  public static Collection<Object[]> testCases() throws IOException {
    Collection<Object[]> ret = new ArrayList<Object[]>();

    File baseDir = new File("src/test/resources/regression");

    String[] asmFiles = baseDir.list((f, n) -> n.endsWith(".txt"));

    for (String f : asmFiles) {
      File asmFile = new File("src/test/resources/regression/" + f);

      String asm = new String(Files.readAllBytes(asmFile.toPath()));
      List<Check> checks = new ArrayList<Check>();
      if(asm.contains(".assert")) {
        String assertSection = asm.substring(asm.indexOf(".assert") + 7).trim();
        List<String> lines = Arrays.asList(assertSection.split("\n")).stream().filter(s -> !s.trim().isEmpty())
            .collect(Collectors.toList());
        for (String line : lines) {
          String[] parts = line.split("=");
          if (parts.length != 2) {
            throw new IllegalArgumentException("assert doesn't have exactly one equals sign: " + line);
          }
          String reg = parts[0].trim();
          reg = reg.replace("$", "");
          String val = parts[1].trim();

          RegCheck check = new RegCheck(reg, Integer.valueOf(val));
          checks.add(check);
        }
        asm = asm.substring(0, asm.indexOf(".assert"));
      } else {
        throw new IllegalArgumentException(".assert section must exist at the end!");
      }
      
      ret.add(new Object[] { f.replace(".txt", ""), asm, checks });
    }

    return ret;
  }

  @Test
  public void test() {
    MipsParser parser = new MipsParser();
    MipsParserResult res = parser.parse(asmContents);

    CircSimulation sim = new CircSimulation(new File("mips.circ"), res.getProgram(), res.getData());

    sim.run(new EndReached());

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

}
