package sample;
import com.phidgets.*;
import com.phidgets.event.*;
import java.util.*;

public class DigitalOutput extends Thread {

  private InterfaceKitPhidget ik;
  private Integer[] forceSensorData = new Integer[4];
  private Main parent;
  private int m_Result = 0;
  private boolean stopped = false;

  DigitalOutput (Main parent) {
    this.parent = parent;
  }

  public int getResult() {
    return m_Result;
  }
  /**
   *
   * @param out output port
   */
  public void makeSound(int out, int duration) {
    int count = 0;
    long startTime = System.currentTimeMillis();
    while (System.currentTimeMillis() - startTime <= duration) {
      try {
        boolean state = Math.abs(Math.round(Math.sin(Math.sin(count)) * 1000)) != 0;
        ik.setOutputState(out, state);
        count++;
      } catch(Exception e) {
        System.out.println(e);
      }

      duration--;
    }
  }

  public Integer[] getForceSensorData() {
    return this.forceSensorData;
  }

  public boolean initialise() throws Exception {
    System.out.println(Phidget.getLibraryVersion());


    ik = new InterfaceKitPhidget();

    ik.addAttachListener(new AttachListener() {
      public void attached(AttachEvent ae) {
        //System.out.println("attachment of " + ae);
      }
    });

    ik.addDetachListener(new DetachListener() {
      public void detached(DetachEvent ae) {
        //System.out.println("detachment of " + ae);
      }
    });

    ik.addErrorListener(new ErrorListener() {
      public void error(ErrorEvent ee) {
        //System.out.println(ee);
      }
    });

    ik.addInputChangeListener(new InputChangeListener() {
      public void inputChanged(InputChangeEvent oe) {
        //System.out.println(oe);
      }
    });

    ik.addOutputChangeListener(new OutputChangeListener() {
      public void outputChanged(OutputChangeEvent oe) {
        //System.out.println(oe);
      }
    });

    ik.addSensorChangeListener(new SensorChangeListener() {
      public void sensorChanged(SensorChangeEvent se) {
        forceSensorData[se.getIndex() % 4] = se.getValue();

        int avg = ((forceSensorData[0] + forceSensorData[2]) / 2) + ((forceSensorData[1] + forceSensorData[3]) / 2);
        m_Result = avg;

        try {
          ik.setOutputState(1, avg > 950);
        } catch (PhidgetException e) {
          e.printStackTrace();
        }

        parent.printMsg(Integer.toString(avg));
      }
    });

    ik.openAny();
    System.out.println("waiting for InterfaceKit attachment...");
    ik.waitForAttachment();

    System.out.println(ik.getDeviceName());

    Thread.sleep(500);

    return true;
  }

  @Override
  public void run() {
    super.run();

    try {
      initialise();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stopDO() throws PhidgetException {
    System.out.print("closing...");
    ik.setOutputState(1, false);
    ik.close();
    ik = null;
    stopped = true;
    System.out.println(" ok");
  }
}