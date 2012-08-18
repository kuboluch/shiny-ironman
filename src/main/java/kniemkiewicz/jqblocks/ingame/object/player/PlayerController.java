package kniemkiewicz.jqblocks.ingame.object.player;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.HitResolver;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.level.VillageGenerator;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.LadderBackground;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.util.LimitedSpeed;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
@Component
public class PlayerController implements InputListener,HealthController<Player> {

  static final Log logger = LogFactory.getLog(PlayerController.class);

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
  Backgrounds backgrounds;

  @Autowired
  VillageGenerator villageGenerator;

  @Resource
  Sound underAttackSound;

  /**
   * This is manually invoked by Game to make sure that level is created before.
   */
  public void initPlayer() {
    player = new Player();
    player.addTo(renderQueue, movingObjects);
    player.getXMovement().setPos(VillageGenerator.STARTING_X);
    player.getYMovement().setPos(villageGenerator.getStartingY() - Player.HEIGHT - 2);
    player.updateShape();
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
    if (KeyboardUtils.isLeftPressed(input)) {
      player.getXMovement().setAcceleration(-Player.X_ACCELERATION);
    }
    if (KeyboardUtils.isRightPressed(input)) {
      player.getXMovement().setAcceleration(Player.X_ACCELERATION);
    }
    // Are we holding a ladder?
    if (Collections3.findFirst(backgrounds.intersects(player.getShape()), LadderBackground.class) != null) {
      // We shouldn't slow down player if he can walk instead of holding ladder.
      if (flying) {
        player.getXMovement().limitSpeed(Player.MAX_LADDER_SPEED);
      }
      LimitedSpeed yMovement = player.getYMovement();
      if (yMovement.getAcceleration() > 0) {
        yMovement.setAcceleration(0);
      }
      yMovement.setSpeed(0);
      if (KeyboardUtils.isUpPressed(input)) {
        player.getYMovement().setSpeed(-Player.MAX_LADDER_SPEED);
      }
      if (KeyboardUtils.isDownPressed(input)) {
        player.getYMovement().setSpeed(Player.MAX_LADDER_SPEED);
      }
    } else {
      if (KeyboardUtils.isUpPressed(input) && !flying) {
        player.getYMovement().setSpeed(-Player.JUMP_SPEED);
      } else {
        player.getYMovement().setAcceleration(Sizes.G);
      }
    }

    float x = player.getXMovement().getPos();
    float y = player.getYMovement().getPos();
    player.update(delta);
    //having a copy of players shape make debugging easier.
    Rectangle playerShape = GeometryUtils.getNewBoundingRectangle(player.getShape());
    List<Rectangle> collidingRectangles = blocks.getBlocks().getIntersectingRectangles(playerShape);

    for (Rectangle r : collidingRectangles) {
      if(!GeometryUtils.intersects(r, playerShape)) {
        blocks.getBlocks().getIntersectingRectangles(playerShape);
        assert false;
      }
    }
    float newX = player.getXMovement().getPos();
    float newY = player.getYMovement().getPos();
    float dx = newX - x;
    float dy = newY - y;
    logger.debug(player.toString());
    for (Rectangle r : collidingRectangles) {
      HitResolver.resolve(player, dx, dy, r);
      logger.debug(GeometryUtils.toString(r) + " " + player.toString());
      player.updateShape();
    }
    if (Assert.ASSERT_ENABLED) {
      for (Rectangle r : collidingRectangles) {
        assert !GeometryUtils.intersects(r, player.getShape());
      }
    }
    if (Assert.ASSERT_ENABLED) {
      if (blocks.getBlocks().getIntersectingRectangles(player.getShape()).size() > 0) {
        for (Rectangle r : blocks.getBlocks().getIntersectingRectangles(player.getShape())) {
          HitResolver.resolve(player, dx, dy, r);
          player.updateShape();
        }
        assert false;
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

  public void setPlayer(Player player) {
    this.player = player;
  }

  @Override
  public void killed(Player object) {
    // as for now, Player cannot be killed
  }

  @Override
  public void damaged(Player object, Object source, int amount) {
    if (!underAttackSound.playing()) {
      underAttackSound.play(1, 1);
    }
  }
}
