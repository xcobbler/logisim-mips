package com.xcobbler.jhu.logisim;

/**
 * Determines if the program should stop based on the value of the pc register
 * 
 * @author Xavier Coble
 *
 */
public class EndReached implements StopCondition {
  public boolean shouldStop(CircSimulation circ) {
    return circ.getValue("pc") >= circ.getProgramEnd();
  }
}
