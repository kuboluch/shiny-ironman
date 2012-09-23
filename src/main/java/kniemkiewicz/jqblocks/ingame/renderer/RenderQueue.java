package kniemkiewicz.jqblocks.ingame.renderer;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.util.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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

  @Autowired
  CollisionController collisionController;

  @Autowired
  Configuration configuration;

  @Resource
  Image skyBackgroundImage;

  EnumMap<RenderableObject.Layer, Set<RenderableObject>> renderableObjects = new EnumMap<RenderableObject.Layer, Set<RenderableObject>>(RenderableObject.Layer.class);
  Set<Renderable> renderables = new HashSet<Renderable>();

  public static final Color SKY = new Color(26f / 255, 100f / 255, 191f / 255);

  RenderQueue() {
    for (RenderableObject.Layer l : RenderableObject.Layer.values()) {
      renderableObjects.put(l, new HashSet<RenderableObject>());
    }
  }

  boolean SHOW_PICKABLE_QUAD_TREE_BOUNDARIES;
  boolean SHOW_MOVING_QUAD_TREE_BOUNDARIES;

  @PostConstruct
  void init() {
    SHOW_PICKABLE_QUAD_TREE_BOUNDARIES = configuration.getBoolean("RenderQueue.SHOW_PICKABLE_QUAD_TREE_BOUNDARIES", false);
    SHOW_MOVING_QUAD_TREE_BOUNDARIES = configuration.getBoolean("RenderQueue.SHOW_MOVING_QUAD_TREE_BOUNDARIES", false);
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
      ((ObjectRenderer)beanProvider.getBean(renderer, true)).render(r, g,  pov);
    } else {
      r.renderObject(g, pov);
    }
  }

  public void render(Graphics g) {
    skyBackgroundImage.draw(0,0, pointOfView.getWindowWidth(), pointOfView.getWindowHeight());
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
    if (SHOW_PICKABLE_QUAD_TREE_BOUNDARIES) {
      g.setColor(Color.red);
      for (Rectangle r : collisionController.getRectsFor(CollisionController.ObjectType.PICKABLE)) {
        g.draw(r);
      }
      g.setColor(Color.orange);
      g.setLineWidth(2);
      for (QuadTree.HasShape ob : collisionController.getAll(MovingObjects.PICKABLE)) {
        g.draw(ob.getShape());
      }
      g.setLineWidth(1);
    }
    if (SHOW_MOVING_QUAD_TREE_BOUNDARIES) {
      g.setColor(Color.black);
      for (Rectangle r : collisionController.getRectsFor(CollisionController.ObjectType.MOVING_OBJECT)) {
        g.draw(r);
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