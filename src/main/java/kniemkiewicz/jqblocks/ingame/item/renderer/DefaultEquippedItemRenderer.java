package kniemkiewicz.jqblocks.ingame.item.renderer;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.util.Pair;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
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

  public Pair<Integer, Integer> getDefaultXY() {
    if (isLeftFaced()) {
      return Pair.newInstance(pointOfView.getWindowWidth() / 2 - SIZE + 6 , 2 + pointOfView.getWindowHeight() / 2 - SIZE);
    } else {
      return Pair.newInstance(pointOfView.getWindowWidth() / 2 - 6, 2 + pointOfView.getWindowHeight() / 2 - SIZE);
    }
  }

  @Override
  public void renderEquippedItem(Item item, Graphics g) {
    ItemRenderer<Item> renderer = springBeanProvider.getBean(item.getItemRenderer(), true);
    Pair<Integer, Integer> xy = getDefaultXY();
    renderer.renderItem(item, xy.getFirst(), xy.getSecond(), SIZE, isLeftFaced());
  }
}
