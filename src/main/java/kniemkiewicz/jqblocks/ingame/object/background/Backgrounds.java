package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.background.NaturalDirtBackground;
import kniemkiewicz.jqblocks.ingame.object.background.Tree;
import kniemkiewicz.jqblocks.ingame.util.LinearIntersectionIterator;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

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

  Set<RenderableObject> backgrounds = new HashSet<RenderableObject>();

  public void add(RenderableObject background) {
    queue.add(background);
    backgrounds.add(background);
  }

  public void remove(RenderableObject background) {
    backgrounds.remove(background);
    queue.remove(background);
  }

  public Iterator<RenderableObject> intersects(Rectangle rect) {
    return new LinearIntersectionIterator<RenderableObject>(backgrounds.iterator(), rect);
  }

  @Resource(name = "treeImage")
  private Image treeImage;

  public NaturalDirtBackground getNaturalDirtBackground(float x, float y, float width, float height) {
    return new NaturalDirtBackground(x, y, width, height);
  }

  public Tree getTree(int x, int y) {
    return new Tree(x, y, treeImage);
  }
}
