package kniemkiewicz.jqblocks.ingame.inventory.item.renderer;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.util.Pair;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import kniemkiewicz.jqblocks.util.Vector2i;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/28/12
 */
@Component
public class DefaultEquippedItemRenderer implements EquippedItemRenderer<Item> {

  @Autowired
  PlayerController playerController;

  @Autowired
  SpringBeanProvider springBeanProvider;

  @Autowired
  PointOfView pointOfView;

  public static final int SIZE = 2 * Sizes.BLOCK;

  public boolean isLeftFaced() {
    return playerController.getPlayer().isLeftFaced();
  }

  public Vector2i getDefaultXY() {
    if (isLeftFaced()) {
      return new Vector2i(pointOfView.getWindowWidth() / 2 - SIZE + 6, 2 + pointOfView.getWindowHeight() / 2 - SIZE);
    } else {
      return new Vector2i(pointOfView.getWindowWidth() / 2 - 6, 2 + pointOfView.getWindowHeight() / 2 - SIZE);
    }
  }

  @Override
  public void renderEquippedItem(Item item, Graphics g) {
    ItemRenderer<Item> renderer = springBeanProvider.getBean(item.getItemRenderer(), true);
    Vector2i xy = getDefaultXY();
    renderer.renderItem(item, xy.getX(), xy.getY(), SIZE, isLeftFaced());
  }
}
