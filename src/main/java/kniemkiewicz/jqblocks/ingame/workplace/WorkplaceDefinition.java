package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.action.Interactive;
import kniemkiewicz.jqblocks.ingame.ui.renderer.TwlImage;
import kniemkiewicz.jqblocks.ingame.ui.widget.model.PanelItemModel;
import kniemkiewicz.jqblocks.util.Assert;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.annotation.Required;

/**
 * User: qba
 * Date: 05.08.12
 */
public class WorkplaceDefinition implements PanelItemModel, Interactive {

  private String name;
  private String description;
  private int blockWidth = -1;
  private int blockHeight = -1;
  private ImageRenderer renderer;
  private Interactive actionController;

  public WorkplaceDefinition() {}

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public int getWidth() {
    return blockWidth * Sizes.BLOCK;
  }

  public int getHeight() {
    return blockHeight * Sizes.BLOCK;
  }

  @Override
  public Image getImage() {
    return renderer.getImage();
  }

  public ImageRenderer getRenderer() {
    return renderer;
  }

  public PlaceableWorkplaceObject getPlaceableObject(int x, int y, WorkplaceController controller) {
    return new PlaceableWorkplaceObject(this, x, y, renderer, controller);
  }

  @Override
  public boolean canInteract() {
    return actionController.canInteract();
  }

  @Override
  public void interact() {
    actionController.interact();
  }

  @Override
  public int getDurationToComplete() {
    return actionController.getDurationToComplete();
  }

  // Setters

  @Required
  public void setName(String name) {
    Assert.assertTrue(this.name == null, "Workplace definition property change is illegal");
    this.name = name;
  }

  @Required
  public void setDescription(String description) {
    Assert.assertTrue(this.description == null, "Workplace definition property change is illegal");
    this.description = description;
  }

  @Required
  public void setBlockWidth(int blockWidth) {
    Assert.assertTrue(this.blockWidth == -1, "Workplace definition property change is illegal");
    Assert.assertTrue(blockWidth > 0, "Workplace definition blockWidth has to be greater than 0");
    this.blockWidth = blockWidth;
  }

  @Required
  public void setBlockHeight(int blockHeight) {
    Assert.assertTrue(this.blockHeight == -1, "Workplace definition property change is illegal");
    Assert.assertTrue(blockWidth > 0, "Workplace definition blockHeight has to be greater than 0");
    this.blockHeight = blockHeight;
  }

  @Required
  public void setRenderer(ImageRenderer renderer) throws IllegalAccessException {
    Assert.assertTrue(this.renderer == null, "Workplace definition property change is illegal");
    this.renderer = renderer;
  }

  @Required
  public void setActionController(Interactive actionController) {
    Assert.assertTrue(this.actionController == null, "Workplace definition property change is illegal");
    this.actionController = actionController;
  }
}
