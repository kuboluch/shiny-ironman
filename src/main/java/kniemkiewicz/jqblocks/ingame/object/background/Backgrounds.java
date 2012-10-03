package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.LinearIntersectionIterator;
import kniemkiewicz.jqblocks.util.IterableIterator;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
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

  Set<BackgroundElement> backgrounds = new HashSet<BackgroundElement>();

  public Backgrounds() { }

  protected Backgrounds(Set<BackgroundElement> backgrounds) {
    this.backgrounds = backgrounds;
  }

  public void add(BackgroundElement background) {
    queue.add(background);
    backgrounds.add(background);
  }

  public void remove(BackgroundElement background) {
    backgrounds.remove(background);
    queue.remove(background);
  }

  public IterableIterator<BackgroundElement> intersects(Shape rect) {
    return new LinearIntersectionIterator<BackgroundElement>(backgrounds.iterator(), rect);
  }

  public Iterator<BackgroundElement> iterateAll() {
    return backgrounds.iterator();
  }
}
