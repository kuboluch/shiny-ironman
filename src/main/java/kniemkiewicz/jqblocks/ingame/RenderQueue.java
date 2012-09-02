package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import kniemkiewicz.jqblocks.util.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
@Component
public class RenderQueue {

  @Autowired
  PointOfView pointOfView;

  @Autowired
  SpringBeanProvider beanProvider;

  EnumMap<RenderableObject.Layer, Set<RenderableObject>> renderableObjects = new EnumMap<RenderableObject.Layer, Set<RenderableObject>>(RenderableObject.Layer.class);
  List<Renderable> renderables = new ArrayList<Renderable>();

  public static final Color SKY = new Color(26f / 255, 100f / 255, 191f / 255);

  RenderQueue() {
    for (RenderableObject.Layer l : RenderableObject.Layer.values()) {
      renderableObjects.put(l, new HashSet<RenderableObject>());
    }
  }

  public void add(RenderableObject renderable) {
    assert renderable.getRenderer() == null || beanProvider.getBean(renderable.getRenderer(), true) != null;
    renderableObjects.get(renderable.getLayer()).add(renderable);
  }

  public void add(Renderable renderable) {
    renderables.add(renderable);
  }

  public void doRender(RenderableObject r, Graphics g, PointOfView pov) {
    BeanName renderer = r.getRenderer();
    if (renderer != null) {
      ((ObjectRenderer) beanProvider.getBean(renderer, true)).render(r, g, pov);
    } else {
      r.renderObject(g, pov);
    }
  }

  public void render(Graphics g) {
    g.setBackground(SKY);
    g.setLineWidth(1);
    g.translate(-pointOfView.getShiftX(), -pointOfView.getShiftY());
    Rectangle window = new Rectangle(pointOfView.getShiftX(), pointOfView.getShiftY(), pointOfView.getWindowWidth(), pointOfView.getWindowHeight());
    for (RenderableObject.Layer l : RenderableObject.Layer.values()) {
      for (RenderableObject r : renderableObjects.get(l)) {
        if (GeometryUtils.intersects(window, r.getShape())) {
          doRender(r, g, pointOfView);
        }
      }
    }
    g.translate(pointOfView.getShiftX(), pointOfView.getShiftY());
    Iterator<Renderable> iter = renderables.iterator();
    while (iter.hasNext()) {
      Renderable r = iter.next();
      r.render(g);
      if (r.isDisposable()) {
        iter.remove();
      }
    }
  }

  public void remove(RenderableObject renderable) {
    renderableObjects.get(renderable.getLayer()).remove(renderable);
  }

  public IterableIterator<?> iterateAllObjects() {
    List<Set<RenderableObject>> sets = new ArrayList<Set<RenderableObject>>();
    sets.addAll(renderableObjects.values());
    return Collections3.iterateOverAll(sets.iterator());
  }
}
