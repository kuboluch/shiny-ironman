package kniemkiewicz.jqblocks.ingame.content.player;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderBackground;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.controller.HitResolver;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.controller.SoundController;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseMovedEvent;
import kniemkiewicz.jqblocks.ingame.level.LevelGenerator;
import kniemkiewicz.jqblocks.ingame.level.VillageGenerator;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.movement.SingleAxisMovement;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Input;
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

  @Autowired
  SoundController soundController;

  @Autowired
  CollisionController collisionController;

  @Autowired
  ControllerUtils controllerUtils;

  @Autowired
  EventBus eventBus;

  /**
   * This is manually invoked by Game to make sure that level is created before.
   */
  public void initPlayer() {
    player = new Player();
    player.addTo(renderQueue, movingObjects);
    player.getFullXYMovement().getXMovement().setPos(VillageGenerator.STARTING_X);
    player.getFullXYMovement().getYMovement().setPos(villageGenerator.getStartingY() - Player.HEIGHT - 2);
    player.updateShape();
  }

  public void listen(Input input, int delta) {
    XYMovement playerMovement = player.getFullXYMovement();
    SingleAxisMovement xMovement = playerMovement.getXMovement();
    SingleAxisMovement yMovement = playerMovement.getYMovement();

    listenToControls(input, xMovement, yMovement);

    float x = xMovement.getPos();
    float y = yMovement.getPos();
    player.update(delta);
    //having a copy of players shape make debugging easier.
    Rectangle playerShape = GeometryUtils.getNewBoundingRectangle(player.getShape());
    logger.debug(player.toString());
    collideWithLevelWalls(playerShape, xMovement.getPos() - x, yMovement.getPos() - y);
    collideWithBlocks(playerShape, xMovement.getPos() - x, yMovement.getPos() - y);

    // Do not change this without a good reason. May lead to screen flickering in rare conditions.
    int centerX = (int)xMovement.getPos() + Player.WIDTH / 2;
    int centerY = (int)yMovement.getPos() + Player.HEIGHT / 2;
    pointOfView.setCenter(centerX, centerY);
  }

  private void collideWithLevelWalls(Rectangle playerShape, float dx, float dy) {
    for (QuadTree.HasShape po : collisionController.fullSearch(LevelGenerator.LEVEL_WALLS, playerShape)) {
      HitResolver.resolve(player, dx, dy, GeometryUtils.getBoundingRectangle(po.getShape()));
    }
  }

  private void collideWithBlocks(Rectangle playerShape, float dx, float dy) {
    List<Rectangle> collidingRectangles = blocks.getBlocks().getIntersectingRectangles(playerShape);

    for (Rectangle r : collidingRectangles) {
      if(!GeometryUtils.intersects(r, playerShape)) {
        blocks.getBlocks().getIntersectingRectangles(playerShape);
        assert false;
      }
    }
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
  }

  private void listenToControls(Input input, SingleAxisMovement xMovement, SingleAxisMovement yMovement) {
    MouseMovedEvent mme = eventBus.getLatestMouseMovedEvent();
    xMovement.setDirection(mme.getNewScreenX() >= pointOfView.getWindowWidth() / 2);
    boolean flying = controllerUtils.isFlying(player.getShape());
    if (KeyboardUtils.isLeftPressed(input)) {
      xMovement.accelerateNegative();
    }
    if (KeyboardUtils.isRightPressed(input)) {
      xMovement.acceleratePositive();
    }
    // Are we holding a ladder?
    if (Collections3.findFirst(backgrounds.intersects(player.getShape()), LadderBackground.class) != null) {
      // We shouldn't slow down player if he can walk instead of holding ladder.
      if (flying) {
        xMovement.limitSpeed(Player.MAX_LADDER_SPEED);
      }
      if (yMovement.getAcceleration() > 0) {
        yMovement.setAcceleration(0);
      }
      yMovement.setSpeed(0);
      if (KeyboardUtils.isUpPressed(input)) {
        yMovement.setSpeed(-Player.MAX_LADDER_SPEED);
      }
      if (KeyboardUtils.isDownPressed(input)) {
        yMovement.setSpeed(Player.MAX_LADDER_SPEED);
      }
    } else {
      if (KeyboardUtils.isUpPressed(input) && !flying) {
        yMovement.setSpeed(-Player.JUMP_SPEED);
      } else {
        yMovement.setAcceleration(Sizes.G);
      }
    }
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  @Override
  public void killed(Player object, QuadTree.HasShape source) {
    // as for now, Player cannot be killed
  }

  @Override
  public void damaged(Player object, QuadTree.HasShape source, int amount) {
    soundController.playUnique(underAttackSound);
    controllerUtils.pushFrom(player, source, ControllerUtils.DEFAULT_PUSH_BACK);
  }
}
