package kniemkiewicz.jqblocks.ingame.resource.inventory;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.inventory.AbstractInventory;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.EmptyItem;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemInventory;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceItem;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ResourceInventory extends AbstractInventory<ResourceItem> implements Inventory<ResourceItem>, Renderable {

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

  private static class ResourceEmptyItem extends EmptyItem implements ResourceItem {
    @Override
    public Resource getResource() {
      return null;
    }
  }

  protected final ResourceItem emptyItem = new ResourceEmptyItem();

  @PostConstruct
  void init() {
    size = 2;
    renderQueue.add(this);
    for (int i = 0; i < size; i++) {
      items.add(getEmptyItem());
    }
  }

  @Override
  protected ResourceItem getEmptyItem() {
    return emptyItem;
  }

  final static private String[] ids = {"F1", "F2", "F3"};

  public void render(Graphics g) {
    int x = pointOfView.getWindowWidth() - items.size() * LARGE_SQUARE_SIZE - (items.size() - 1) * SQUARE_DIST - X_MARGIN;
    int y = ItemInventory.SQUARE_SIZE + 3 * Y_MARGIN;
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
      ItemRenderer<Item> renderer = springBeanProvider.getBean(item.getItemRenderer(), true);
      renderer.renderItem(item, g, x + SQUARE_ROUNDING, y + SQUARE_ROUNDING, square_size - 2 * SQUARE_ROUNDING, false);
      g.setColor(Color.black);
      g.drawString(ids[i], x - 5, y - 4);
      x += SQUARE_DIST + square_size;
      i += 1;
    }
  }
}
