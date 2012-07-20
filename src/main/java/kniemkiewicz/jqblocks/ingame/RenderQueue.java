package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
@Component
public class RenderQueue implements Renderable {

  @Autowired
  PointOfView pointOfView;

  Set<RenderableObject> renderableObjects = new HashSet<RenderableObject>();
  Set<Renderable> renderables = new HashSet<Renderable>();
  
  public static final Color SKY = new Color(26f/255, 100f/255, 191f/255);
  
  public void add(RenderableObject renderable) {
    renderableObjects.add(renderable);
  }

  public void add(Renderable renderable) {
    renderables.add(renderable);
  }

  public void render(Graphics g) {
    g.setBackground(SKY);
    g.setLineWidth(1);
    g.translate(-pointOfView.getShiftX(), -pointOfView.getShiftY());
    Rectangle window = new Rectangle(pointOfView.getShiftX(), pointOfView.getShiftY(), Sizes.WINDOW_WIDTH, Sizes.WINDOW_HEIGHT);
    for (RenderableObject r : renderableObjects) {
      if (window.intersects(r.getShape())) {
        r.renderObject(g, pointOfView);
      }
    }
    g.translate(pointOfView.getShiftX(), pointOfView.getShiftY());
    for (Renderable r : renderables) {
      r.render(g);
    }
  }

  public void remove(RenderableObject block) {
    renderableObjects.remove(block);
  }
}
