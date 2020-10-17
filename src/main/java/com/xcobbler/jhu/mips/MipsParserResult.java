package com.xcobbler.jhu.mips;

import java.util.ArrayList;
import java.util.List;

public class MipsParserResult {
  private MipsProgram program;
  private MipsData data;
  private List<String> errors = new ArrayList<String>();

  public MipsProgram getProgram() {
    return program;
  }

  public void setProgram(MipsProgram program) {
    this.program = program;
  }

  public MipsData getData() {
    return data;
  }

  public void setData(MipsData data) {
    this.data = data;
  }

  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    this.errors = errors;
  }
}
