package kniemkiewicz.jqblocks.ingame.object.arrow;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 7/21/12
 */
@Component
public class ArrowController implements UpdateQueue.UpdateController<Arrow>{

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  SolidBlocks blocks;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  UpdateQueue updateQueue;

  public void add(Arrow arrow) {
    updateQueue.add(arrow);
    renderQueue.add(arrow);
  }


  private boolean checkArrowHit(Arrow arrow) {
    for (AbstractBlock b : blocks.intersects(GeometryUtils.getBoundingRectangle(arrow.getShape()))) {
      if (GeometryUtils.intersects(b.getShape(), arrow.getShape())) {
        if (b != arrow.getSource()) {
          return true;
        }
      }
    }
    for (PhysicalObject b : movingObjects.intersects(arrow.getShape())) {
      if (GeometryUtils.intersects(b.getShape(), arrow.getShape())) {
        if (b != arrow.getSource()) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void update(Arrow arrow, int delta) {
    arrow.update(delta);
    if (checkArrowHit(arrow)) {
      updateQueue.remove(arrow);
    }
  }
}
