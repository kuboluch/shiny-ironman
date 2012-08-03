package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.LinearIntersectionIterator;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: knie
 * Date: 7/20/12
 */

/**
 * This class will handle all more complicated logic about backgrounds. For now it just passes
 * all of them to renderQueue.
 */
@Component
public class Backgrounds {
  @Autowired
  RenderQueue queue;

  Set<AbstractBackgroundElement> backgrounds = new HashSet<AbstractBackgroundElement>();

  public void add(AbstractBackgroundElement background) {
    queue.add(background);
    backgrounds.add(background);
  }

  public void remove(AbstractBackgroundElement background) {
    backgrounds.remove(background);
    queue.remove(background);
  }

  public Iterator<AbstractBackgroundElement> intersects(Rectangle rect) {
    return new LinearIntersectionIterator<AbstractBackgroundElement>(backgrounds.iterator(), rect);
  }
}
