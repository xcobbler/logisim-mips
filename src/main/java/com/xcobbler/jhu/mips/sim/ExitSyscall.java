package com.xcobbler.jhu.mips.sim;

/**
 * Determines if the program should stop based on the value of the pc register
 * 
 * @author Xavier Coble
 *
 */
public class ExitSyscall implements StopCondition {
  private static final String SYSCALL = "0000000c";
  public boolean shouldStop(CircSimulation circ) {
    String prev = circ.getPreviousInstruction();
    int v0 = circ.getValue("v0");
    if (v0 == 10 && SYSCALL.equals(prev)) {
      return true;
    } else {
      return false;
    }
  }
}
