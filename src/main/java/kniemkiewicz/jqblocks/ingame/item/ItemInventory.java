package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryBase;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: krzysiek
 * Date: 10.07.12
 */
@Component
public class ItemInventory extends InventoryBase<Item> implements Renderable {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  SpringBeanProvider springBeanProvider;

  public static int SQUARE_SIZE = 25;
  public static int LARGE_SQUARE_SIZE = 40;
  public static int SQUARE_DIST = 10;
  public static int SQUARE_ROUNDING = 3;
  public static int Y_MARGIN = 5;
  public static int X_MARGIN = 10;

  @PostConstruct
  void init() {
    renderQueue.add(this);
    for (int i = 0; i < getSize(); i++) {
      items.add(emptyItem);
    }
    Assert.executeAndAssert(add(new DirtBlockItem()));
    Assert.executeAndAssert(add(new PickaxeItem()));
    Assert.executeAndAssert(add(new AxeItem()));
    Assert.executeAndAssert(add(new BowItem()));
    Assert.executeAndAssert(add(new PickaxeItem(1000000)));
  }

  final static private String[] ids = {"1", "2","3","4","5","6","7","8","9","0"};

  public void render(Graphics g) {

    int x = pointOfView.getWindowWidth() - items.size() * SQUARE_SIZE - (items.size() - 1) * SQUARE_DIST - X_MARGIN;
    int i = 0;
    for (Item item : items) {
      int square_size = SQUARE_SIZE;
      if (i == selectedIndex) {
        g.setColor(Color.lightGray);
      } else {
        g.setColor(Color.gray);
      }
      g.fillRoundRect(x, Y_MARGIN, square_size, square_size, SQUARE_ROUNDING);
      if (i == selectedIndex) {
        g.setColor(Color.black);
      } else {
        g.setColor(Color.lightGray);
      }
      g.drawRoundRect(x, Y_MARGIN, square_size, square_size, SQUARE_ROUNDING);
      if (item.getItemRenderer() != null) {
        ItemRenderer<Item> renderer = springBeanProvider.getBean(item.getItemRenderer(), true);
        renderer.renderItem(item, g, x + SQUARE_ROUNDING, Y_MARGIN + SQUARE_ROUNDING, square_size - 2 * SQUARE_ROUNDING);
      } else {
        item.renderItem(g, x + SQUARE_ROUNDING, Y_MARGIN + SQUARE_ROUNDING, square_size - 2 * SQUARE_ROUNDING);
      }
      g.setColor(Color.black);
      g.drawString(ids[i], x - 5, Y_MARGIN - 4);
      x += SQUARE_DIST + square_size;
      i += 1;
    }
  }
}

