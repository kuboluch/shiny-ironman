package kniemkiewicz.jqblocks.ingame.inventory.item;

import com.google.common.collect.ImmutableList;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.production.ItemProductionRequirements;
import kniemkiewicz.jqblocks.ingame.production.ResourceRequirement;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.hud.widget.model.PanelItemModel;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceDefinition;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * User: qba
 * Date: 21.08.12
 */
public class ItemDefinition implements PanelItemModel, ItemFactory, ItemProductionRequirements {

  String name;
  String description;
  ImageRenderer renderer;
  ItemFactory itemFactory;
  ItemProductionRequirements productionRequirements;
  Boolean globallyAvailable;
  List<WorkplaceDefinition> productionPlaces;
  Integer productionDuration;

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
    return productionRequirements.getResourceRequirements();
  }

  @Override
  public List<Item> getItemRequirements() {
    return productionRequirements.getItemRequirements();
  }

  public int getProductionDuration() {
    if (productionDuration == null) {
      return Sizes.DEFAULT_PRODUCTION_DURATION;
    }
    return productionDuration;
  }

  public boolean isGloballyAvailable() {
    if (globallyAvailable == null) {
      globallyAvailable = false;
    }
    return globallyAvailable;
  }

  public boolean canBeProducedIn(WorkplaceDefinition workplaceDefinition) {
    if (isGloballyAvailable()) return true;
    if (productionPlaces != null) {
      return !Collections3.collectSubclasses(productionPlaces, workplaceDefinition.getClass()).isEmpty();
    }
    return false;
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
  public void setProductionRequirements(ItemProductionRequirements productionRequirements) {
    Assert.assertTrue(this.productionRequirements == null, "Item definition property change is illegal");
    this.productionRequirements = productionRequirements;
  }

  public void setGloballyAvailable(boolean globallyAvailable) {
    Assert.assertTrue(this.globallyAvailable == null, "Item definition property change is illegal");
    this.globallyAvailable = globallyAvailable;
  }

  public void setProductionPlaces(List<WorkplaceDefinition> productionPlaces) {
    Assert.assertTrue(this.productionPlaces == null, "Item definition property change is illegal");
    this.productionPlaces = ImmutableList.copyOf(productionPlaces);
  }

  public void setProductionDuration(Integer productionDuration) {
    Assert.assertTrue(this.productionDuration == null, "Item definition property change is illegal");
    this.productionDuration = productionDuration;
  }
}
