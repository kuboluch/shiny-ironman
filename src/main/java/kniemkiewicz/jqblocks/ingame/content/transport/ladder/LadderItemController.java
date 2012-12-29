package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.inventory.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
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
  protected void startAction() {
  }

  @Override
  protected void stopAction() {
  }

  @Override
  protected void updateAction(LadderItem item, int delta) {
  }

  @Override
  protected boolean isActionCompleted() {
    return true;
  }

  @Override
  protected void onAction() {
    backgrounds.add(new LadderBackground((int) affectedRectangle.getX(), (int) affectedRectangle.getY()));
  }

  @Override
  public DroppableObject getObject(LadderItem item, int centerX, int centerY) {
    return new Ladder(centerX, centerY);
  }
}
