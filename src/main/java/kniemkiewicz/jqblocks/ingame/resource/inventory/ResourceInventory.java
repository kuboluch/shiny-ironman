package kniemkiewicz.jqblocks.ingame.resource.inventory;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryBase;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ResourceInventory extends InventoryBase implements Renderable {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  SpringBeanProvider springBeanProvider;

  public static int LARGE_SQUARE_SIZE = 40;
  public static int SQUARE_DIST = 10;
  public static int SQUARE_ROUNDING = 3;
  public static int Y_MARGIN = 5;
  public static int X_MARGIN = 10;

  @PostConstruct
  void init() {
    size = 2;
    renderQueue.add(this);
    for (int i = 0; i < size; i++) {
      items.add(emptyItem);
    }
  }

  public boolean add(Item item) {
    assert Assert.validateSerializable(item);
    Assert.assertTrue(selectedIndex >= 0);
    Assert.assertTrue(selectedIndex < size);
    if (items.get(selectedIndex) == emptyItem) {
      items.set(selectedIndex, item);
      return true;
    }
    return false;
  }

  final static private String[] ids = {"F1", "F2", "F3"};

  public void render(Graphics g) {

    int x = pointOfView.getWindowWidth() - items.size() * LARGE_SQUARE_SIZE - (items.size() - 1) * SQUARE_DIST - X_MARGIN;
    int y = pointOfView.getWindowHeight() - LARGE_SQUARE_SIZE - Y_MARGIN;
    int i = 0;
    for (Item item : items) {
      int square_size = LARGE_SQUARE_SIZE;
      if (i == selectedIndex) {
        g.setColor(Color.lightGray);
      } else {
        g.setColor(Color.gray);
      }
      g.fillRoundRect(x, y, square_size, square_size, SQUARE_ROUNDING);
      if (i == selectedIndex) {
        g.setColor(Color.black);
      } else {
        g.setColor(Color.lightGray);
      }
      g.drawRoundRect(x, y, square_size, square_size, SQUARE_ROUNDING);
      if (item.getItemRenderer() != null) {
        ItemRenderer<Item> renderer = springBeanProvider.getBean(item.getItemRenderer(), true);
        renderer.renderItem(item, g, x + SQUARE_ROUNDING, y + SQUARE_ROUNDING, square_size - 2 * SQUARE_ROUNDING);
      } else {
        item.renderItem(g, x + SQUARE_ROUNDING, y + SQUARE_ROUNDING, square_size - 2 * SQUARE_ROUNDING);
      }
      g.setColor(Color.black);
      g.drawString(ids[i], x - 5, y - 4);
      x += SQUARE_DIST + square_size;
      i += 1;
    }
  }
}
