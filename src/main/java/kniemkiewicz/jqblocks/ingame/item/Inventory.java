package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.util.Assert;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * User: krzysiek
 * Date: 10.07.12
 */
@Component
public class Inventory implements Renderable {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  PointOfView pointOfView;

  @Resource(name = "pickaxeImage")
  private Image pickaxeImage;

  List<Item> items = new ArrayList<Item>();
  public static int SQUARE_SIZE = 25;
  public static int LARGE_SQUARE_SIZE = 40;
  public static int SQUARE_DIST = 10;
  public static int SQUARE_ROUNDING = 3;
  public static int Y_MARGIN = 5;
  public static int X_MARGIN = 10;
  public static final int SIZE = 10;
  int selectedIndex = 0;

  private final Item emptyItem = new EmptyItem();

  @PostConstruct
  void init() {
    renderQueue.add(this);
    for (int i = 0; i < SIZE; i++) {
      items.add(emptyItem);
    }
    Assert.executeAndAssert(add(new DirtBlockItem()));
    Assert.executeAndAssert(add(new PickaxeItem(pickaxeImage)));
    Assert.executeAndAssert(add(new AxeItem(pickaxeImage)));
    Assert.executeAndAssert(add(new BowItem()));
    Assert.executeAndAssert(add(new PickaxeItem(1000000, pickaxeImage)));
  }

  public boolean add(Item item) {
    int newIndex = -1;
    if (item.isLarge()) {
      if (items.get(0) != emptyItem) return false;
      newIndex = 0;
    } else {
      for (int i = 1;i < SIZE; i++) {
        if (items.get(i) == emptyItem) {
          newIndex = i;
          break;
        }
      }
      if (newIndex < 0) {
        // No small slot found, trying big one.
        if (items.get(0) != emptyItem) return false;
        newIndex = 0;
      }
    }
    items.set(newIndex, item);
    if (items.get(selectedIndex) == emptyItem) {
      selectedIndex = newIndex;
    }
    return true;
  }

  public void setSelectedIndex(int x) {
    selectedIndex = x;
  }

  public Item getSelectedItem() {
    return items.get(selectedIndex);
  }

  final static private String[] ids = {"1", "2","3","4","5","6","7","8","9","0"};
  public void render(Graphics g) {

    int x = pointOfView.getWindowWidth() - items.size() * SQUARE_SIZE - (items.size() - 1) * SQUARE_DIST - X_MARGIN - LARGE_SQUARE_SIZE + SQUARE_SIZE;
    int i = 0;
    for (Item item : items) {
      int square_size = i == 0 ? LARGE_SQUARE_SIZE : SQUARE_SIZE;
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
      item.renderItem(g, x + SQUARE_ROUNDING, Y_MARGIN + SQUARE_ROUNDING, square_size - 2 * SQUARE_ROUNDING);
      g.setColor(Color.black);
      g.drawString(ids[i], x - 5, Y_MARGIN - 4);
      x += SQUARE_DIST + square_size;
      i += 1;
    }
  }
}

