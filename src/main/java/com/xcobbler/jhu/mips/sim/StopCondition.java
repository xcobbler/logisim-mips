package com.xcobbler.jhu.mips.sim;

public interface StopCondition {
  boolean shouldStop(CircSimulation circ);
}
