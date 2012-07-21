package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.object.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.Arrow;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: knie
 * Date: 7/21/12
 */
@Component
public class ArrowController {
  LinkedList<Arrow> arrows = new LinkedList<Arrow>();

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  SolidBlocks blocks;

  @Autowired
  MovingObjects movingObjects;

  public void add(Arrow arrow) {
    arrows.add(arrow);
    renderQueue.add(arrow);
  }


  private boolean checkArrowHit(Arrow arrow) {
    for (AbstractBlock b : Collections3.getIterable(blocks.intersects(GeometryUtils.getBoundingRectangle(arrow.getShape())))) {
      if (b.getShape().intersects(arrow.getShape())) {
        if (b != arrow.getSource()) {
          return true;
        }
      }
    }
    for (PhysicalObject b : Collections3.getIterable(movingObjects.intersects(arrow.getShape()))) {
      if (b.getShape().intersects(arrow.getShape())) {
        if (b != arrow.getSource()) {
          return true;
        }
      }
    }
    return false;
  }

  public void update(int delta) {
    Iterator<Arrow> it = arrows.iterator();
    while (it.hasNext()) {
      Arrow arrow = it.next();
      arrow.update(delta);
      if (checkArrowHit(arrow)) {
        it.remove();
      }
    }
  }
}
