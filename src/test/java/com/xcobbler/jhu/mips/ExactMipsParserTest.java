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

@RunWith(Parameterized.class)
public class ExactMipsParserTest {

  private String textName = null;
  private String asmContents = null;
  private String expectedProgram = null;
  private String expectedData = null;

  public ExactMipsParserTest(String testName, String asmContents, String expectedProgram, String expectedData) {
    this.textName = testName;
    this.asmContents = asmContents;
    this.expectedProgram = expectedProgram;
    this.expectedData = expectedData;
  }

  @Parameterized.Parameters(name = "{index} - {0}")
  public static Collection<Object[]> testCases() throws IOException {
    Collection<Object[]> ret = new ArrayList<Object[]>();
    
    File baseDir = new File("src/test/resources/exact-sample-mips");

    String[] asmFiles = baseDir.list((f, n) -> n.endsWith(".asm"));
    
    for (String f : asmFiles) {
      File asmFile = new File("src/test/resources/exact-sample-mips/" + f);
      File programFile = new File("src/test/resources/exact-sample-mips/" + f.replace(".asm", "") + "-program-hex.txt");
      File dataFile = new File("src/test/resources/exact-sample-mips/" + f.replace(".asm", "") + "-data-hex.txt");

      String asm = new String(Files.readAllBytes(asmFile.toPath()));
      String program = new String(Files.readAllBytes(programFile.toPath()));
      String data = "";
      if (dataFile.exists()) {
        data = new String(Files.readAllBytes(dataFile.toPath()));
      }

      ret.add(new Object[] { f.replace(".asm", ""), asm, program, data });
    }
    
    return ret;
  }

  @Test
  public void test() {
    MipsParser parser = new MipsParser();
    MipsParserResult res = parser.parse(asmContents);

    List<String> instructions = res.getProgram().getWords();
    List<String> data = res.getData().getWords();

    List<String> programWords = Arrays.asList(expectedProgram.split("\n")).stream().map(s -> s.trim())
        .filter(s -> !s.isEmpty()).collect(Collectors.toList());
    List<String> dataWords = Arrays.asList(expectedData.split("\n")).stream().map(s -> s.trim())
        .filter(s -> !s.isEmpty()).collect(Collectors.toList());
    
    Assert.assertEquals(programWords, instructions);
    Assert.assertEquals(dataWords, data);
  }

}
