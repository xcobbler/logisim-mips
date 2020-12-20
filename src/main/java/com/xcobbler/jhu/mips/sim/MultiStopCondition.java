package com.xcobbler.jhu.mips.sim;

import java.util.ArrayList;
import java.util.List;

public class MultiStopCondition implements StopCondition {
  private List<StopCondition> conds = new ArrayList<StopCondition>();

  public MultiStopCondition(List<StopCondition> conds) {
    this.conds = conds;
  }

  @Override
  public boolean shouldStop(CircSimulation circ) {
    for (StopCondition c : conds) {
      if (c.shouldStop(circ)) {
        return true;
      }
    }
    return false;
  }

}
