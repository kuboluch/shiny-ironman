package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.item.controller.AxeItemController;
import kniemkiewicz.jqblocks.ingame.item.controller.PickaxeItemController;
import kniemkiewicz.jqblocks.ingame.item.feature.Strength;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class AxeItem implements Item, Strength, UpdateQueue.ToBeUpdated<AxeItem> {

  private Image image;

  private int strength = Sizes.DEFAULT_AXE_STRENGTH;

  public AxeItem(Image image) {
    this.image = image;
  }

  public AxeItem(int strength, Image image) {
    this.strength = strength;
    this.image = image;
  }

  public int getStrength() {
    return strength;
  }

  public void renderItem(Graphics g, int x, int y, int square_size) {
    image.draw(x,y,square_size, square_size);
  }

  @Override
  public Class<AxeItemController> getController() {
    return AxeItemController.class;
  }

  @Override
  public boolean isLarge() {
    return false;
  }
}
