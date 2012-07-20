package kniemkiewicz.jqblocks.ingame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
@Component
public class ClickCounter implements MouseListener {
  public static Log logger = LogFactory.getLog(ClickCounter.class);

  @Autowired
  PointOfView pointOfView;

  final List<MouseClickListener> listeners = new ArrayList<MouseClickListener>();
  final Object clicksLock = new Object();
  List<MouseClickListener.Click> clicks = new ArrayList<MouseClickListener.Click>();

  public void mouseWheelMoved(int change) { }

  public void mouseClicked(int button, int x, int y, int clickCount) {
    // left clicks only, for now.
    if (button != 0) return;
    synchronized (clicksLock) {
      clicks.add(new MouseClickListener.Click(x + pointOfView.getShiftX(), y + pointOfView.getShiftY(), clickCount));
    }
  }

  public void add(MouseClickListener mouseClickListener) {
    listeners.add(mouseClickListener);
  }

  public void update() {
    List<MouseClickListener.Click> oldClicks;
    synchronized (clicksLock) {
      oldClicks = clicks;
      clicks = new ArrayList<MouseClickListener.Click>();
    }
    for (MouseClickListener l : listeners) {
      l.listen(oldClicks);
    }
  }

  public void mousePressed(int button, int x, int y) { }

  public void mouseReleased(int button, int x, int y) {  }

  public void mouseMoved(int oldx, int oldy, int newx, int newy) {  }

  public void mouseDragged(int oldx, int oldy, int newx, int newy) {  }

  public void setInput(Input input) {  }

  public boolean isAcceptingInput() {
    return true;
  }

  public void inputEnded() {  }

  public void inputStarted() {  }
}
