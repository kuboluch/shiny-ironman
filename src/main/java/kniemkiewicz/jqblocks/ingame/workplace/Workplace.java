package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.action.Interactive;
import kniemkiewicz.jqblocks.ingame.ui.renderer.Image;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 05.08.12
 */
public class Workplace implements Interactive {
  private String name;
  private String description;
  private int blockWidth;
  private int blockHeight;
  private ImageRenderer renderer;
  private Image uiImage;
  private Interactive actionController;

  public Workplace(String name, String description, int blockWidth, int blockHeight, ImageRenderer renderer, Image uiImage, Interactive actionController) {
    this.name = name;
    this.description = description;
    this.blockWidth = blockWidth;
    this.blockHeight = blockHeight;
    this.renderer = renderer;
    this.uiImage = uiImage;
    this.actionController = actionController;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getBlockWidth() {
    return blockWidth;
  }

  public int getBlockHeight() {
    return blockHeight;
  }

  public Image getUIImage() {
    return uiImage;
  }

  public ImageRenderer getRenderer() {
    return renderer;
  }

  public PlaceableWorkplaceObject getPlaceableObject(int x, int y, WorkplaceController controller) {
    return new PlaceableWorkplaceObject(this, x, y, renderer, controller);
  }

  public void interact() {
    actionController.interact();
  }
}
