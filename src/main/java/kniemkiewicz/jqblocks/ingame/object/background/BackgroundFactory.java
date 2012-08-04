package kniemkiewicz.jqblocks.ingame.object.background;

import org.springframework.stereotype.Component;

// TODO: Drop?
@Component
public class BackgroundFactory {

  public NaturalDirtBackground getNaturalDirtBackground(float x, float y, float width, float height) {
    return new NaturalDirtBackground(x, y, width, height);
  }

  public Tree getTree(int x, int y) {
    return new Tree(x, y);
  }
}
