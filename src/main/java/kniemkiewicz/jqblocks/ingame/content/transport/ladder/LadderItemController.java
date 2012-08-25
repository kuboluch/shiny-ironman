package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.LadderBackground;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 15.08.12
 */
@Component
public class LadderItemController extends AbstractActionItemController<LadderItem> {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  SolidBlocks blocks;

  @Autowired
  Backgrounds backgrounds;

  @Override
  protected boolean canPerformAction(int x, int y) {
    Rectangle rectangle = getAffectedRectangle(x, y);
    if (backgrounds.intersects(rectangle).hasNext()) return false;
    return !blocks.getBlocks().collidesWithNonEmpty(rectangle);
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    return new LadderBackground(x, y).getShape();
  }

  @Override
  protected void startAction(LadderItem item) { }

  @Override
  protected void stopAction(LadderItem item) {}

  @Override
  protected void updateAction(LadderItem item, int delta) { }

  @Override
  protected boolean isActionCompleted() {
    return true;
  }

  @Override
  protected void onAction() {
    new LadderBackground((int)affectedRectangle.getX(), (int)affectedRectangle.getY()).addTo(backgrounds);
  }

  @Override
  public DroppableObject getObject(LadderItem item, int centerX, int centerY) {
    return new Ladder(centerX, centerY);
  }
}