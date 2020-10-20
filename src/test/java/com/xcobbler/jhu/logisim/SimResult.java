package com.xcobbler.jhu.logisim;

public class SimResult {
  private long cycles = 0;

  public SimResult(long cycles) {
    this.cycles = cycles;
  }

  public long getCycles() {
    return cycles;
  }

}
