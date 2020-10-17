package com.xcobbler.jhu.logisim;

import java.util.ArrayList;
import java.util.List;

import com.xcobbler.jhu.mips.MipsData;
import com.xcobbler.jhu.mips.MipsProgram;

public class LogisimCirc {
  private List<Register> registers = new ArrayList<Register>();
  private MipsProgram program;
  private MipsData data;

  public LogisimCirc(MipsProgram program, MipsData data) {
    this.program = program;
    this.data = data;
  }

  public Register getRegister(String name) {
    return null;
  }
}
