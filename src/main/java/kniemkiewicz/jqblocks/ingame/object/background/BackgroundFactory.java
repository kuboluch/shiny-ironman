package kniemkiewicz.jqblocks.ingame.object.background;

import org.newdawn.slick.Image;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
