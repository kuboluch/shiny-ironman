package kniemkiewicz.jqblocks.ingame.input;

import org.newdawn.slick.Input;
import org.springframework.stereotype.Component;

@Component
public class InputContainer {

  private Input input;

  public void setInput(Input input) {
    this.input = input;
  }

  public Input getInput() {
    return input;
  }
}
