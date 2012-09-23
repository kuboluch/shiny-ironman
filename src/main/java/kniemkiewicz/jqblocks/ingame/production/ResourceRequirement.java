package kniemkiewicz.jqblocks.ingame.production;

import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.resource.renderer.ResourceRenderer;
import kniemkiewicz.jqblocks.ingame.hud.widget.model.PanelItemModel;
import org.newdawn.slick.Image;

/**
 * User: qba
 * Date: 26.08.12
 */
public class ResourceRequirement implements PanelItemModel {

  Resource resource;

  ResourceRenderer renderer;

  ResourceStorageController resourceStorageController;

  public ResourceRequirement(Resource resource, ResourceRenderer renderer, ResourceStorageController resourceStorageController) {
    this.resource = resource;
    this.renderer = renderer;
    this.resourceStorageController = resourceStorageController;
  }

  public Resource getResource() {
    return resource;
  }

  public boolean isFulfilled() {
    return resourceStorageController.hasEnoughResource(resource);
  }

  @Override
  public String getName() {
    return resource.getType().getName();
  }

  @Override
  public String getDescription() {
    return resource.getType().getDescription();
  }

  @Override
  public Image getImage() {
    return renderer.getImage(resource.getType());
  }

  @Override
  public int getWidth() {
    return -1;
  }

  @Override
  public int getHeight() {
    return -1;
  }
}
