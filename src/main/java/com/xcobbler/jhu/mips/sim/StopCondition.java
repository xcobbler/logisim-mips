package com.xcobbler.jhu.mips.sim;

public interface StopCondition {
  /**
   * 
   * @param circ the CPU state
   * @return whether the simulation should stop.
   */
  boolean shouldStop(CircSimulation circ);
}
