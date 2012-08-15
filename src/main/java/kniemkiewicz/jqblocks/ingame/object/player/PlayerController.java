package kniemkiewicz.jqblocks.ingame.object.player;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.HitResolver;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.level.VillageGenerator;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
  RenderQueue renderQueue;

  @Autowired
  VillageGenerator villageGenerator;

  /**
   * This is manually invoked by Game to make sure that level is created before.
   */
  public void initPlayer() {
    player = new Player();
    player.addTo(renderQueue, movingObjects);
    player.getXMovement().setPos(VillageGenerator.STARTING_X);
    player.getYMovement().setPos(villageGenerator.getStartingY() - Player.HEIGHT - 3);
  }

  public void listen(Input input, int delta) {
    /*
    if (KeyboardUtils.isDownPressed(input)) {
      player.setY(player.getY() + delta);
    }
    */
    Rectangle belowPlayer = new Rectangle(player.getShape().getMinX() + 1, player.getShape().getMaxY() + 2,
        player.getShape().getWidth() - 4, 0);
    boolean flying = !blocks.getBlocks().collidesWithNonEmpty(belowPlayer);
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
    List<Rectangle> collidingRectangles = blocks.getBlocks().getIntersectingRectangles(player.getShape());

    for (Rectangle r : collidingRectangles) {
      HitResolver.resolve(player, player.getXMovement().getPos() - x, player.getYMovement().getPos() - y, r);
      player.updateShape();
    }

    assert blocks.getBlocks().getIntersectingRectangles(player.getShape()).size() == 0;
    // Do not change this without a good reason. May lead to screen flickering in rare conditions.
    int centerX = (int) player.getXMovement().getPos() + Player.WIDTH / 2;
    int centerY = (int) player.getYMovement().getPos() + Player.HEIGHT / 2;
    pointOfView.setCenter(centerX, centerY);
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}
