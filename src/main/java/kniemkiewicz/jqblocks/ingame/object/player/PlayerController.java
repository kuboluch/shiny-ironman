package kniemkiewicz.jqblocks.ingame.object.player;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.controller.HitResolver;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.item.Inventory;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.object.ObjectKiller;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
@Component
public class PlayerController implements InputListener {

  Player player;

  @Autowired
  SolidBlocks blocks;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  ObjectKiller objectKiller;

  @Autowired
  Inventory inventory;

  @Autowired
  RenderQueue renderQueue;

  /**
   * This is manually invoked by Game to make sure that level is created before.
   */
  public void init() {
    player = new Player();
    player.addTo(renderQueue, movingObjects);
    int x = (Sizes.MIN_X + Sizes.MAX_X) / 2;
    player.getXMovement().setPos(x);

    Rectangle rect = new Rectangle(player.getShape().getX(), Sizes.MIN_Y, player.getShape().getWidth(), Sizes.MAX_Y - Sizes.MIN_Y);
    // All blocks in the same column as player.
    Iterator<AbstractBlock> iter = blocks.intersects(rect);
    float minY = Sizes.MAX_Y;
    while (iter.hasNext()) {
      AbstractBlock b = iter.next();
      if (b.getShape().getMinY() < minY) {
        minY = b.getShape().getMinY();
      }
    }
    player.getYMovement().setPos(minY - Player.HEIGHT - 5);
  }

  public void listen(Input input, int delta) {
    /*
    if (KeyboardUtils.isDownPressed(input)) {
      player.setY(player.getY() + delta);
    }
    */
    Rectangle belowPlayer = new Rectangle(player.getShape().getMinX(),player.getShape().getMaxY() + 2,
        player.getShape().getWidth(), 0);
    Iterator<AbstractBlock> sb = blocks.intersects(belowPlayer);
    boolean flying = !sb.hasNext();
    if (KeyboardUtils.isUpPressed(input) && !flying) {
      player.getYMovement().setSpeed(-Player.JUMP_SPEED);
    } else {
      player.getYMovement().setAcceleration(Sizes.G);
    }
    if (KeyboardUtils.isLeftPressed(input)) {
      player.getXMovement().setAcceleration(-Player.X_ACCELERATION);
    }
    if (KeyboardUtils.isRightPressed(input)) {
      player.getXMovement().setAcceleration(Player.X_ACCELERATION);
    }

    float x = player.getXMovement().getPos();
    float y = player.getYMovement().getPos();
    player.update(delta);
    List<AbstractBlock> colliding_blocks = Collections3.getList(blocks.intersects(player.getShape()));

    for (AbstractBlock b : colliding_blocks) {
      HitResolver.resolve(player, player.getXMovement().getPos() - x, player.getYMovement().getPos() - y, b.getShape());
      player.updateShape();
    }
    for (AbstractBlock b : colliding_blocks) {
      assert !GeometryUtils.intersects(player.getShape(), b.getShape());
    }
    Iterator<PhysicalObject> it  = movingObjects.intersects(player.getShape());
    while (it.hasNext()) {
      PhysicalObject po = it.next();
      if (po instanceof PickableObject) {
        Item item = ((PickableObject)po).getItem();
        if (inventory.add(item)) {
          it.remove();
          objectKiller.killMovingObject(po);
        }
      }
    }

    // Do not change this without a good reason. May lead to screen flickering in rare conditions.
    int centerX = (int)player.getXMovement().getPos() + Player.WIDTH / 2;
    int centerY = (int)player.getYMovement().getPos() + Player.HEIGHT / 2;
    pointOfView.setCenter(centerX, centerY);
  }

  public Player getPlayer() {
    return player;
  }
}
