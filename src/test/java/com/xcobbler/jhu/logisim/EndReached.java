package com.xcobbler.jhu.logisim;

public class EndReached implements StopCondition {
  public boolean shouldStop(CircSimulation circ) {
    return circ.getValue("pc") >= circ.getProgramEnd();
  }
}
