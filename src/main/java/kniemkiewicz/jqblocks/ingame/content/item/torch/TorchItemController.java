package kniemkiewicz.jqblocks.ingame.content.item.torch;

import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 29.08.12
 */
@Component
public class TorchItemController extends AbstractActionItemController<TorchItem> {

  @Autowired
  Backgrounds backgrounds;

  @Autowired
  SolidBlocks blocks;

  @Override
  protected boolean canPerformAction(int x, int y) {
    Rectangle rectangle = getAffectedRectangle(x, y);
    if (!blocks.isOnSolidGround(rectangle)) return false;
    if (backgrounds.intersects(rectangle).hasNext()) return false;
    return !blocks.getBlocks().collidesWithNonEmpty(rectangle);
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    return new TorchBackground(x, y).getShape();
  }

  @Override
  protected void startAction(TorchItem item) {
  }

  @Override
  protected void stopAction(TorchItem item) {
  }

  @Override
  protected void updateAction(TorchItem item, int delta) {
  }

  @Override
  protected boolean isActionCompleted() {
    return true;
  }

  @Override
  protected void onAction() {
    backgrounds.add(new TorchBackground((int) affectedRectangle.getX(), (int) affectedRectangle.getY()));
  }

  @Override
  public DroppableObject getObject(TorchItem item, int centerX, int centerY) {
    return new Torch(centerX, centerY);
  }
}
