package kniemkiewicz.jqblocks.ingame.ui;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MouseInputInfo implements MouseListener, Renderable {

  private int x;
  private int y;

  @Autowired
  PointOfView pointOfView;

  public void mouseWheelMoved(int change) { }

  public void mouseClicked(int button, int x, int y, int clickCount) { }

  public void mousePressed(int button, int x, int y) { }

  public void mouseReleased(int button, int x, int y) {  }

  public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    x = newx + pointOfView.getShiftX();
    y = newy + pointOfView.getShiftY();
  }

  public void mouseDragged(int oldx, int oldy, int newx, int newy) {  }

  public void setInput(Input input) {  }

  public boolean isAcceptingInput() {
    return true;
  }

  public void inputEnded() {  }

  public void inputStarted() {  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.white);
    g.drawString("mouseX : " + x, 150, 25);
    g.drawString("mouseY : " + y, 150, 38);
  }
}
