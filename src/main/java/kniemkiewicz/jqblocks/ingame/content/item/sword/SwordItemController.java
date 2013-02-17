package kniemkiewicz.jqblocks.ingame.content.item.sword;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.content.resource.Wood;
import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.inventory.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.inventory.item.controller.AbstractActionItemRectangleController;
import kniemkiewicz.jqblocks.ingame.object.DebugRenderableShape;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.ProgressBar;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.ResourceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventoryController;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceObject;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.Vector2i;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class SwordItemController extends AbstractActionItemController<SwordItem> {

  private static final int TOTAL_TIME = 1100;

  private static final int LENGTH = (int) (SwordRenderer.SIZE * Math.sqrt(2) - 5);

  @Autowired
  PlayerController playerController;

  @Autowired
  ControllerUtils controllerUtils;

  SwordItemController() {
    setMinDuration(TOTAL_TIME);
    setPeriodic(true);
  }

  @Override
  protected boolean canPerformAction(int x, int y) {
    return true;
  }

  @Override
  protected void startAction() {
    item.setMoving(true);
  }

  private void doDamage() {
    Player p = playerController.getPlayer();
    Vector2i xy;
    if (p.isLeftFaced()) {
      xy = new Vector2i((int)p.getShape().getCenterX() + 8, (int)p.getShape().getCenterY() + 4);
    } else {
      xy = new Vector2i((int)p.getShape().getCenterX() - 8, (int)p.getShape().getCenterY() - 4);
    }
    float dx = (float) Math.cos(Math.PI / 180 * item.getArc()) * LENGTH;
    float dy = (float) Math.sin(Math.PI / 180 * item.getArc()) * LENGTH;

    if (p.isLeftFaced()) {
      dx = -dx;
    }

    Line line = new Line(xy.getX(), xy.getY(), xy.getX() + dx, xy.getY() + dy);
    controllerUtils.damageTouched(p, line, SwordItem.DAMAGE, false);
  }

  @Override
  protected void stopAction() {
    item.setArc(0);
    item.setMoving(false);
  }

  @Override
  protected void updateAction(SwordItem item, int delta) {
    long time = timeController.getTime() - startTime;

    if (time < 500) {
      item.setArc(item.getArc() - delta / 2);
    } else if (time > TOTAL_TIME - 100) {
      item.setArc(0);
    } else {
      item.setArc(item.getArc() + delta / 1.5);
      doDamage();
    }
    if (item.getArc() < - 90) {
      item.setArc(- 90);
    } else if (item.getArc() > 60) {
      item.setArc(60);
    }
  }

  @Override
  protected boolean isActionCompleted() {
    return false;
  }

  @Override
  public DroppableObject getObject(SwordItem item, int centerX, int centerY) {
    return null;
  }
}
