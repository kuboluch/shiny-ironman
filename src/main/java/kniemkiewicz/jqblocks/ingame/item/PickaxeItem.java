package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.controller.PickaxeItemController;
import kniemkiewicz.jqblocks.ingame.item.feature.Strength;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
public class PickaxeItem implements Item, Strength, UpdateQueue.ToBeUpdated<PickaxeItem> {

  private Image image;

  private int strength = Sizes.DEFAULT_PICKAXE_STRENGTH;

  public PickaxeItem(Image image) {
    this.image = image;
  }

  public PickaxeItem(int strength, Image image) {
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
  public Class<? extends ItemController> getItemController() {
    return PickaxeItemController.class;
  }

  @Override
  public Class<PickaxeItemController> getUpdateController() {
    return PickaxeItemController.class;
  }

  @Override
  public boolean isLarge() {
    return false;
  }
}
