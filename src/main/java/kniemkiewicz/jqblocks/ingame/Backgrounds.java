package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.background.NaturalDirtBackground;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 7/20/12
 */

/**
 * This class will handle all more complicated logic about backgrounds. For now it just passes
 * all of them to renderQueue.
 */
@Component
public class Backgrounds{
  @Autowired
  RenderQueue queue;

  public void add(RenderableObject background) {
    queue.add(background);
  }
}
