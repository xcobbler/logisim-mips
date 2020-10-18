package com.xcobbler.jhu.logisim;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
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

public class CircSimulation {
  private File circ;
  private MipsProgram program;
  private MipsData data;
  private AtomicBoolean doTick = new AtomicBoolean(true);

  private Map<String, Component> registers = new HashMap<String, Component>();
  private Project project;

//  private List<File> tempFiles = new ArrayList<File>();

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

//    try {

      CircuitState state = project.getCircuitState();

      Circuit circ = state.getCircuit();

      Component clock = null;

      Set<Component> nonwires = circ.getNonWires();

      List<Component> rams = new ArrayList<Component>();

      if (nonwires != null) {
        for (Component c : nonwires) {
//          System.out.println("component = " + c);
//          System.out.println("fac = " + c.getFactory());
          if (c.getFactory() != null) {
            if ("RAM".equals(c.getFactory().getName())) {
              rams.add(c);
            } else if ("Register".equals(c.getFactory().getName())) {
              Object label = state.getInstanceState(c).getAttributeValue(c.getAttributeSet().getAttribute("label"));

              registers.put(String.valueOf(label), c);
            } else if("Clock".equals(c.getFactory().getName())) {
              if(clock != null) {
                Location l1 = clock.getLocation();
                Location l2 = c.getLocation();
                throw new SimulationException("Ciruit has multiple clocks! first: " + l1 + "  second: " + l2);
              }
              clock = c;
            }
          }
        }
      }

      if (rams.size() == 2) {
        // the RAM closest to the clock is assumed to the program RAM and the other is the data
        rams = sortRams(rams, clock);

        loadRam(rams.get(0), program);
        // TODO load all zeros for null
        if (data != null) {
          loadRam(rams.get(1), data);
        }

      } else {
        throw new SimulationException("expected the number of RAM elements to 2, but got: " + rams.size());
      }
      sim.removeSimulatorListener(listener);
      sim.addSimulatorListener(listener);

//      System.out.println("end");
//    } finally {
//      sim.shutDown();
//    }

    // all registers get the value 0
    // find program text RAM and load it
    // find program data RAM and load it
  }

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

  public int getValue(String name) {
    Component reg = registers.get(name);
    if (reg != null) {
      Object data = project.getCircuitState().getData(reg);

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

  public int getProgramEnd() {
    return program.getWords().size();
  }

  public void run(StopCondition cond) {
    // TODO should limit the program to like 3 seconds
    while (true) {
      if (doTick.compareAndSet(true, false)) {
        if (cond.shouldStop(this)) {
          break;
        }
        // perform step
        project.getSimulator().tick();
      }
    }
  }

  public void reset(MipsProgram program, MipsData data) throws LoadFailedException {
    this.program = program;
    this.data = data;
    init();
  }

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
//      System.out.println("tickCompleted: " + new Date());
    }

    public void simulatorStateChanged(SimulatorEvent e) {
//      System.out.println("simulatorStateChanged: " + new Date());
    }

    public void propagationCompleted(SimulatorEvent e) {
//      System.out.println("propagationCompleted: " + new Date());
      doTick.set(true);
    }
  };

}
