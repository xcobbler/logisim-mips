package com.xcobbler.jhu.mips.sim;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cburch.draw.shapes.LineUtil;
import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.circuit.Simulator;
import com.cburch.logisim.circuit.SimulatorEvent;
import com.cburch.logisim.circuit.SimulatorListener;
import com.cburch.logisim.circuit.SubcircuitFactory;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.comp.ComponentFactory;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.file.LoadFailedException;
import com.cburch.logisim.file.Loader;
import com.cburch.logisim.file.LogisimFile;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.proj.Project;
import com.xcobbler.jhu.mips.MipsData;
import com.xcobbler.jhu.mips.MipsProgram;
import com.xcobbler.jhu.mips.MipsWords;

/**
 * Represents a logisim circuit executing a MIPs program
 * 
 * @author Xavier Coble
 *
 */
public class CircSimulation {
  private File circ;
  private MipsProgram program;
  private MipsData data;
  private AtomicBoolean doTick = new AtomicBoolean(true);

  private Map<String, Component> registers = new HashMap<String, Component>();
  private Map<String, Component> circuitMap = new HashMap<String, Component>();
  private Project project;
  private static final int PROGRAM_POINTER = 0;
  private static final int GLOBAL_POINTER = 2042096;
  private static final int STACK_POINTER = 16777216;

  public CircSimulation(File circ, MipsProgram program, MipsData data) {
    this.circ = circ;
    this.program = program;
    this.data = data;
    init();
  }

  private void init() {
    Loader loader = new Loader(null);
    LogisimFile logFile;
    try {
      logFile = loader.openLogisimFile(circ);
    } catch (LoadFailedException e) {
      throw new SimulationException(e);
    }

    project = new Project(logFile);

    Simulator sim = project.getSimulator();
    sim.setTickFrequency(4000);

    CircuitState state = project.getCircuitState();

    Circuit circ = state.getCircuit();

    Component clock = null;

    Set<Component> nonwires = circ.getNonWires();

    List<Component> rams = new ArrayList<Component>();
    // System.out.println("start init");
    // prevents loops - you only need to visit subcircuits once to get registers
    Set<String> foundSubcircuits = new HashSet<String>();
    if (nonwires != null) {
      for (Component c : nonwires) {
        // System.out.println("component = " + c);
        // System.out.println("fac = " + c.getFactory());
        if (c.getFactory() != null) {
          // System.out.println("factory = " + c.getFactory().getName());
          if ("RAM".equals(c.getFactory().getName())) {
            rams.add(c);
          } else if ("Register".equals(c.getFactory().getName())) {
            Object label = state.getInstanceState(c).getAttributeValue(c.getAttributeSet().getAttribute("label"));

            registers.put(String.valueOf(label), c);
            // circuitMap.put(String.valueOf(label), state);
          } else if ("Clock".equals(c.getFactory().getName())) {
            if (clock != null) {
              Location l1 = clock.getLocation();
              Location l2 = c.getLocation();
              throw new SimulationException("Ciruit has multiple clocks! first: " + l1 + "  second: " + l2);
            }
            clock = c;
          } else if (c.getFactory() instanceof SubcircuitFactory) {
            if (!foundSubcircuits.contains(c.getFactory().getName())) {
              foundSubcircuits.add(c.getFactory().getName());
              Circuit subcircuit = ((SubcircuitFactory) c.getFactory()).getSubcircuit();

              Set<Component> nonWires2 = subcircuit.getNonWires();
              for (Component c2 : nonWires2) {
                if ("Register".equals(c2.getFactory().getName())) {
                  Object label = state.getInstanceState(c2)
                      .getAttributeValue(c2.getAttributeSet().getAttribute("label"));
                  // System.out.println("nested reg = " + String.valueOf(label));
                  registers.put(String.valueOf(label), c2);
                  circuitMap.put(String.valueOf(label), c);
                }
              }

            }
          }
        }
      }
    }

    // set initial register values
    // ugly hack to force the sub circuit (register block) to have non-null data
    // this may force the circuit to run 0x00000000 ... but in mips this is shift left logical of the zero register
    // and the result stored in the zero register (which should not be allowed)
    sim.removeSimulatorListener(listener);
    sim.addSimulatorListener(listener);
    boolean done = false;
    while (true) {
      if (doTick.compareAndSet(true, false)) {
        if (done) {
          break;
        } else {
          done = true;
          project.getSimulator().tick();
        }
      }
    }
    doTick.set(true);

    setValue("pc", PROGRAM_POINTER);
    setValue("gp", GLOBAL_POINTER);
    setValue("sp", STACK_POINTER);

    if (rams.size() == 2) {
      // the RAM closest to the clock is assumed to the program RAM and the other is the data
      rams = sortRams(rams, clock);

      loadRam(rams.get(0), program);
      // not needed to load zeros when null, because logisim will do this for us
      if (data != null) {
        loadRam(rams.get(1), data);
      }

    } else {
      throw new SimulationException("expected the number of RAM elements to 2, but got: " + rams.size());
    }

    // all registers get the value 0
    // find program text RAM and load it
    // find program data RAM and load it
  }

  /**
   * only works for registers inside the main circuit
   */
  public void printRegisters() {

    CircuitState state = project.getCircuitState();

    Circuit circ = state.getCircuit();

    Component clock = null;

    Set<Component> nonwires = circ.getNonWires();

    List<Component> rams = new ArrayList<Component>();

    if (nonwires != null) {
      for (Component c : nonwires) {
        if (c.getFactory() != null) {
          if ("RAM".equals(c.getFactory().getName())) {
            rams.add(c);
          } else if ("Register".equals(c.getFactory().getName())) {
            Object label = state.getInstanceState(c).getAttributeValue(c.getAttributeSet().getAttribute("label"));

            registers.put(String.valueOf(label), c);
            int val = getValue(String.valueOf(label));
            System.out.println(label + ": " + val);
          } else if ("Clock".equals(c.getFactory().getName())) {
            if (clock != null) {
              Location l1 = clock.getLocation();
              Location l2 = c.getLocation();
              throw new SimulationException("Ciruit has multiple clocks! first: " + l1 + "  second: " + l2);
            }
            clock = c;
          }
        }
      }
    }
  }

  /**
   * 
   * @param The name the name of the register
   * @return The value of the register
   */
  public int getValue(String name) {
    Component reg = registers.get(name);
    if (reg != null) {
      Object data = project.getCircuitState().getData(reg);
      if (data == null && circuitMap.containsKey(name)) {
        data = ((CircuitState) project.getCircuitState().getData(circuitMap.get(name))).getData(reg);
      }

      Object value = 0;

      if (data != null) {
        try {
          Method method = data.getClass().getMethod("getValue");
          method.setAccessible(true);
          value = method.invoke(data);
        } catch (Exception e) {
          throw new SimulationException(e);
        }
      }
      return Integer.valueOf(String.valueOf(value));
    } else {
      throw new SimulationException("There is no register with the name [" + name + "]");
    }
  }

  /**
   * 
   * @param name  The name of the register to set
   * @param value The value to set
   */
  public void setValue(String name, int value) {
    Component reg = registers.get(name);
    if (reg != null) {
      Object data = project.getCircuitState().getData(reg);
      if (data == null && circuitMap.containsKey(name)) {
        data = ((CircuitState) project.getCircuitState().getData(circuitMap.get(name))).getData(reg);
      }

      if (data != null) {
        try {
          Method method = data.getClass().getMethod("setValue", int.class);
          method.setAccessible(true);
          method.invoke(data, value);
        } catch (Exception e) {
          throw new SimulationException(e);
        }
      }
    } else {
      throw new SimulationException("There is no register with the name [" + name + "]");
    }
  }

  public int getProgramEnd() {
    return program.getWords().size();
  }

  public SimResult run(StopCondition cond) {
    // TODO should limit the program to like 3 seconds
    long tickCount = 0;
    // long prev = System.currentTimeMillis();
    while (true) {
      if (doTick.compareAndSet(true, false)) {
        // long end = System.currentTimeMillis();
        // System.out.println("tick = " + (end - prev));

        if (cond.shouldStop(this)) {
          break;
        }
        // prev = System.currentTimeMillis();
        // perform step
        project.getSimulator().tick();
        tickCount++;
      }
    }
    return new SimResult((tickCount) / 2);
  }

  // public void reset(MipsProgram program, MipsData data) throws LoadFailedException {
  // this.program = program;
  // this.data = data;
  // init();
  // }

  /**
   * Sort the RAMs relative to the location to the clock. closest RAMs listed first
   */
  private List<Component> sortRams(List<Component> rams, Component clock){
    List<Component> ret = new ArrayList<Component>();

    ret.addAll(rams);

    ret.sort((a, b) -> (int) (LineUtil.distance(a.getLocation().getX(), a.getLocation().getY(),
        clock.getLocation().getX(), clock.getLocation().getY())
        - LineUtil.distance(b.getLocation().getX(), b.getLocation().getY(), clock.getLocation().getX(),
            clock.getLocation().getY())));

    return ret;
  }

  private void loadRam(Component ram, MipsWords words) {
    ComponentFactory ramFactory = ram.getFactory();
    InstanceState ramState = project.getCircuitState().getInstanceState(ram);

    try {
      List<String> formated = new ArrayList<String>(words.getWords());
      // see: http://www.cburch.com/logisim/docs/2.7/en/html/guide/mem/menu.html
      formated.add(0, "v2.0 raw");

      Path temp = Files.createTempFile("ram-" + System.nanoTime(), null);
      File tempFile = temp.toFile();
      tempFile.deleteOnExit();
      Files.write(temp, formated, StandardOpenOption.WRITE);

      Method method = ramFactory.getClass().getMethod("loadImage", InstanceState.class, File.class);
      method.setAccessible(true);
      method.invoke(ramFactory, ramState, tempFile);

    } catch (Exception e) {
      throw new SimulationException(e);
    }
  }

  private final SimulatorListener listener = new SimulatorListener() {

    public void tickCompleted(SimulatorEvent e) {
      // System.out.println("tickCompleted: " + new Date());
    }

    public void simulatorStateChanged(SimulatorEvent e) {
      // System.out.println("simulatorStateChanged: " + new Date());
    }

    public void propagationCompleted(SimulatorEvent e) {
      // System.out.println("propagationCompleted: " + new Date());
      doTick.set(true);
    }
  };

}
