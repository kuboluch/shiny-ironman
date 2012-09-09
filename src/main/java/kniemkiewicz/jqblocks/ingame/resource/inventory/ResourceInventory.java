package kniemkiewicz.jqblocks.ingame.resource.inventory;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import kniemkiewicz.jqblocks.ingame.inventory.AbstractInventory;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.EmptyItem;
import kniemkiewicz.jqblocks.ingame.item.Item;
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

  public static final int SIZE = 2;

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
  public static int Y_MARGIN = 10;
  public static int X_MARGIN = 10;

  private static class ResourceEmptyItem extends EmptyItem implements ResourceItem {
    @Override
    public Resource getResource() {
      return null;
    }
  }

  protected static final ResourceItem emptyItem = new ResourceEmptyItem();

  public ResourceInventory() {
    super();
  }

  @PostConstruct
  void init() {
    renderQueue.add(this);
  }

  @Override
  protected ResourceItem getEmptyItem() {
    return emptyItem;
  }

  @Override
  public int getSize() {
    return SIZE;
  }

  final static private String[] ids = {"F1", "F2", "F3"};

  @Override
  public void render(Graphics g) {
    int x = pointOfView.getWindowWidth() - items.size() * LARGE_SQUARE_SIZE - (items.size() - 1) * SQUARE_DIST - X_MARGIN;
    int y = SQUARE_SIZE + 3 * Y_MARGIN;
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
      renderer.renderItem(item, x + SQUARE_ROUNDING, y + SQUARE_ROUNDING, square_size - 2 * SQUARE_ROUNDING, false);
      g.setColor(Color.black);
      g.drawString(ids[i], x - 5, y - 4);
      x += SQUARE_DIST + square_size;
      i += 1;
    }
  }

  @Override
  public boolean isDisposable() {
    return false;
  }
}
