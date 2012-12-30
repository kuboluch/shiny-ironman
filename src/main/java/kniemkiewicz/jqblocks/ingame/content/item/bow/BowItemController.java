package kniemkiewicz.jqblocks.ingame.content.item.bow;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.controller.ProjectileController;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.item.arrow.Arrow;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.controller.SoundController;
import kniemkiewicz.jqblocks.ingame.inventory.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.inventory.item.controller.AbstractActionItemRectangleController;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: knie
 * Date: 7/21/12
 */

@Component
public class BowItemController extends AbstractActionItemController<BowItem> {

  @Autowired
  PlayerController playerController;

  @Autowired
  ProjectileController projectileController;

  @Autowired
  PointOfView pointOfView;

  @Resource
  Sound bowSound;

  @Resource
  Sound bowStringSound;

  @Autowired
  SoundController soundController;

  @Autowired
  ControllerUtils controllerUtils;

  private static float SPEED = Sizes.MAX_FALL_SPEED / 1.5f;

  public Vector2f getLevelBowPosition() {
    Rectangle shape = playerController.getPlayer().getShape();
    float dx = -Sizes.BLOCK / 2;
    if (playerController.getPlayer().isLeftFaced()) {
      dx *= -1;
    }
    return new Vector2f(shape.getCenterX() + dx, shape.getCenterY() - Sizes.BLOCK / 2);
  }

  public Vector2f getScreenBowPosition() {
    float dx = -Sizes.BLOCK / 2;
    if (playerController.getPlayer().isLeftFaced()) {
      dx *= -1;
    }
    return new Vector2f(pointOfView.getWindowWidth()/ 2 + dx, pointOfView.getWindowHeight() / 2 - Sizes.BLOCK / 2);
  }

  private void shotArrow() {
    Vector2f pos = getLevelBowPosition();
    Vector2f speed = controllerUtils.getCurrentDirection(SPEED, getScreenBowPosition());

    projectileController.add(new Arrow(pos.getX(), pos.getY(), playerController.getPlayer(), speed.getX(), speed.getY()));
    soundController.play(bowSound, 0.7f);
  }

  @Override
  protected boolean canPerformAction(int x, int y) {
    return true;
  }

  @Override
  protected void startAction() {
    soundController.playUnique(bowStringSound);
  }

  @Override
  protected void stopAction() {
    shotArrow();
  }

  @Override
  protected void updateAction(BowItem item, int delta) { }

  @Override
  protected boolean isActionCompleted() {
    return false;
  }

  @Override
  public DroppableObject getObject(BowItem item, int centerX, int centerY) {
    return null;
  }
}