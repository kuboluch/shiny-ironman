package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.ui.renderer.Image;
import kniemkiewicz.jqblocks.ingame.ui.widget.Selectable;

/**
 * User: qba
 * Date: 21.08.12
 */
public class ItemDefinition implements Selectable, ItemFactory {

  private String name;
  private String description;
  private Image uiImage;
  ItemFactory itemFactory;

  protected ItemDefinition(String name, String description, Image uiImage, ItemFactory itemFactory) {
    this.name = name;
    this.description = description;
    this.uiImage = uiImage;
    this.itemFactory = itemFactory;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public Image getUIImage() {
    return uiImage;
  }

  @Override
  public Item createItem() {
    return itemFactory.createItem();
  }
}
