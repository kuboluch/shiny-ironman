package kniemkiewicz.jqblocks.ingame.input;

import org.newdawn.slick.Input;
import org.springframework.stereotype.Component;

@Component
public class MouseInput {

  private Input input;

  public void setInput(Input input) {
    this.input = input;
  }

  public int getAbsoluteMouseX() {
    return input.getAbsoluteMouseX();
  }

  public int getAbsoluteMouseY() {
    return input.getAbsoluteMouseY();
  }

  public int getMouseX() {
    return input.getMouseX();
  }

  public int getMouseY() {
    return input.getMouseY();
  }

  public boolean isMouseButtonDown(int button) {
    return input.isMouseButtonDown(button);
  }
}
