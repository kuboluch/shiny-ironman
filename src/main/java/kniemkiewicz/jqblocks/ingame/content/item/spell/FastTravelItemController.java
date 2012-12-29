package kniemkiewicz.jqblocks.ingame.content.item.spell;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.controller.event.Event;
import kniemkiewicz.jqblocks.ingame.controller.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.controller.event.input.mouse.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.level.LevelGenerator;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * User: krzysiek
 * Date: 14.09.12
 */
@Component
public class FastTravelItemController implements ItemController<FastTravelItem> {

  @Autowired
  PlayerController playerController;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  CollisionController collisionController;

  @Autowired
  Configuration configuration;

  int TELEPORT_DISTANCE = -1;

  @PostConstruct
  void init() {
    TELEPORT_DISTANCE = configuration.getInt("FastTravelItemController.TELEPORT_DISTANCE", 2000);
  }

  @Override
  public void listen(FastTravelItem selectedItem, List<Event> events) {
    for (MouseClickEvent ev : Collections3.filter(events, MouseClickEvent.class)) {
      if (ev.getButton() == Button.LEFT) {
        executeTeleport();
      }
    }
  }

  private void executeTeleport() {

    Player player = playerController.getPlayer();
    Vector2f currentPos = new Vector2f(player.getXYMovement().getX(), player.getXYMovement().getY());
    if (!tryExecuteTeleport()) {
      player.getXYMovement().getXMovement().setPos(currentPos.getX());
      player.getXYMovement().getYMovement().setPos(currentPos.getY());
      player.updateShape();
    }
  }

  private boolean tryExecuteTeleport() {
    assert TELEPORT_DISTANCE > 0;
    Player player = playerController.getPlayer();
    int dx = player.isLeftFaced() ? - TELEPORT_DISTANCE : TELEPORT_DISTANCE;
    if (player.getXYMovement().getX() + dx < Sizes.MIN_X + Sizes.BLOCK) return false;
    if (player.getXYMovement().getX() + dx > Sizes.MAX_X - Player.WIDTH - Sizes.BLOCK) return false;
    player.getXYMovement().getXMovement().setPos(player.getXYMovement().getX() + dx);
    player.getXYMovement().getYMovement().setPos(Sizes.MIN_Y + player.getShape().getHeight());
    player.updateShape();
    int y = solidBlocks.getBlocks().getUnscaledDropHeight(player.getShape());
    player.getXYMovement().getYMovement().setPos(y - Player.HEIGHT - 2 * Sizes.BLOCK);
    player.updateShape();
    return collisionController.fullSearch(LevelGenerator.LEVEL_WALLS, player.getShape()).size() == 0;
  }

  @Override
  public DroppableObject getObject(FastTravelItem item, int centerX, int centerY) {
    return null;
  }

  @Override
  public boolean canDeselectItem(FastTravelItem item) {
    return true;
  }
}
