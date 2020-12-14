package com.xcobbler.jhu.mips.sim;

public class SimulationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public SimulationException(String msg) {
    super(msg);
  }

  public SimulationException(Throwable t) {
    super(t);
  }
}
