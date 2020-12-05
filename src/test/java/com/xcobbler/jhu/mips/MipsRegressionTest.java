package com.xcobbler.jhu.mips;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

  @SuppressWarnings("unchecked")
  @Parameterized.Parameters(name = "{index} - {0}")
  public static Collection<Object[]> testCases() throws IOException {
    Collection<Object[]> ret = new ArrayList<Object[]>();

    File baseDir = new File("src/test/resources/regression");

    String[] asmFiles = baseDir.list((f, n) -> n.endsWith(".txt"));

    for (String f : asmFiles) {
      File asmFile = new File("src/test/resources/regression/" + f);

      String asm = new String(Files.readAllBytes(asmFile.toPath()));
      if(asm.contains(".assert")) {
        
//        asm = asm.substring(0, asm.indexOf(".assert"));

        List<String> allLines = Arrays.asList(asm.split("\n")).stream().filter(s -> !s.trim().isEmpty())
            .collect(Collectors.toList());

        List<Object[]> tests = handleSplitting(allLines);

        for (Object[] test : tests) {
          asm = ((List<String>) test[1]).stream().collect(Collectors.joining("\n"));

          String assertSection = asm.substring(asm.indexOf(".assert") + 7).trim();
          List<String> assertLines = Arrays.asList(assertSection.split("\n")).stream().filter(s -> !s.trim().isEmpty())
              .collect(Collectors.toList());
          List<Check> checks = new ArrayList<Check>();
          for (String line : assertLines) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
              continue;
            }
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
          
          ret.add(new Object[] { f.replace(".txt", "") + test[0], asm.substring(0, asm.indexOf(".assert")), checks });
        }

      } else {
        throw new IllegalArgumentException(".assert section must exist at the end!");
      }
    }

    return ret;
  }

  // 0 - String name extension
  // 1 - List<String> lines
  private static List<Object[]> handleSplitting(List<String> lines) {
    List<Object[]> ret = new ArrayList<Object[]>();
    Map<String, List<String>> replacements = new HashMap<String, List<String>>();

    for (String line : lines) {
      line = line.trim();
      if (line.startsWith("$")) {
        String[] parts = line.split("=");
        String valsPart = parts[1].substring(1, parts[1].length() - 1);
        String[] vals = valsPart.split("\\|");
        replacements.put(parts[0].trim(), Arrays.asList(vals));
      } else {
        break;
      }
    }

    lines = lines.subList(replacements.keySet().size(), lines.size());

    if (replacements.keySet().size() == 0) {
      ret.add(new Object[] { "", lines });
    } else {
      List<String> toReplace = lines.subList(replacements.keySet().size(), lines.size());
      // TODO only supports single line replacements
      for (Entry<String, List<String>> entry : replacements.entrySet()) {
        for (String val : entry.getValue()) {
          List<String> subList = new ArrayList<String>();
          for (String s : toReplace) {
            subList.add(s.replace(entry.getKey(), val));
          }
          ret.add(new Object[] { "-" + val.replace("$", ""), subList });
        }
      }
    }

    return ret;
  }

  @Test(timeout = 3000)
  public void test() {
    MipsParser parser = new MipsParser();
    MipsParserResult res = parser.parse(asmContents);

    System.out.println("\nstart: " + textName);
    res.getProgram().getWords().stream().forEach(System.out::println);
    System.out.println("end: " + textName);

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
