package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.production.ItemProductionRequirements;
import kniemkiewicz.jqblocks.ingame.production.ResourceRequirement;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.ui.widget.model.PanelItemModel;
import kniemkiewicz.jqblocks.util.Assert;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * User: qba
 * Date: 21.08.12
 */
public class ItemDefinition implements PanelItemModel, ItemFactory, ItemProductionRequirements {

  private String name;
  private String description;
  private ImageRenderer renderer;
  ItemFactory itemFactory;
  ItemProductionRequirements itemProductionRequirements;

  protected ItemDefinition() {
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Image getImage() {
    return renderer.getImage();
  }

  @Override
  public int getWidth() {
    return -1;
  }

  @Override
  public int getHeight() {
    return -1;
  }

  public ImageRenderer getRenderer() {
    return renderer;
  }

  @Override
  public Item createItem() {
    return itemFactory.createItem();
  }

  @Override
  public List<ResourceRequirement> getResourceRequirements() {
    return itemProductionRequirements.getResourceRequirements();
  }

  @Override
  public List<Item> getItemRequirements() {
    return itemProductionRequirements.getItemRequirements();
  }

  // Setters

  @Required
  public void setName(String name) {
    Assert.assertTrue(this.name == null, "Item definition property change is illegal");
    this.name = name;
  }

  @Required
  public void setDescription(String description) {
    Assert.assertTrue(this.description == null, "Item definition property change is illegal");
    this.description = description;
  }

  @Required
  public void setRenderer(ImageRenderer renderer) {
    Assert.assertTrue(this.renderer == null, "Item definition property change is illegal");
    this.renderer = renderer;
  }

  @Required
  public void setItemFactory(ItemFactory itemFactory) {
    Assert.assertTrue(this.itemFactory == null, "Item definition property change is illegal");
    this.itemFactory = itemFactory;
  }

  @Required
  public void setItemProductionRequirements(ItemProductionRequirements itemProductionRequirements) {
    Assert.assertTrue(this.itemProductionRequirements == null, "Item definition property change is illegal");
    this.itemProductionRequirements = itemProductionRequirements;
  }
}
