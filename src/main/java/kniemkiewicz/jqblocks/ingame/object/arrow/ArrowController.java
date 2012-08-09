package kniemkiewicz.jqblocks.ingame.object.arrow;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.Out;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 7/21/12
 */
@Component
public class ArrowController implements UpdateQueue.UpdateController<Arrow>{

  private int ARROW_DMG = 10;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  SolidBlocks blocks;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  World killer;

  public void add(Arrow arrow) {
    updateQueue.add(arrow);
    renderQueue.add(arrow);
  }


  private boolean checkArrowHit(Arrow arrow, Out<PhysicalObject> physicalObject) {
    if (blocks.getBlocks().collidesWithNonEmpty(GeometryUtils.getBoundingRectangle(arrow.getShape()))) {
      return true;
    }
    for (PhysicalObject b : movingObjects.intersects(arrow.getShape())) {
      if (GeometryUtils.intersects(b.getShape(), arrow.getShape())) {
        if (b != arrow.getSource()) {
          physicalObject.set(b);
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void update(Arrow arrow, int delta) {
    arrow.update(delta);
    Out<PhysicalObject> po = new Out<PhysicalObject>();
    if (checkArrowHit(arrow, po)) {
      if (po.get() != null) {
        renderQueue.remove(arrow);
      }
      updateQueue.remove(arrow);
      if ((po.get() != null) && (po.get() instanceof HasHealthPoints)) {
        ((HasHealthPoints)po.get()).getHp().damage(ARROW_DMG, killer);
      }
    }
  }
}
