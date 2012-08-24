package kniemkiewicz.jqblocks.ingame.resource.renderer;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceObject;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceItem;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.XMLPackedSheet;
import org.newdawn.slick.geom.Rectangle;

import javax.annotation.Resource;

/**
 * User: qba
 * Date: 15.08.12
 */
public class ResourceRenderer implements ItemRenderer<ResourceItem>, ObjectRenderer<ResourceObject> {

  @Resource( name="blockSheet" )
  XMLPackedSheet blockSheet;

  @Override
  public void render(ResourceObject object, Graphics g, PointOfView pov) {
    float percentage = (float) object.getResourceAmount() * 1.0f / object.getResourceMaxPileSize() * 1.0f;
    Rectangle rectangle = GeometryUtils.getBoundingRectangle(object.getShape());
    render(g, object.getResourceType(), percentage, rectangle);
  }

  @Override
  public void renderItem(ResourceItem item, Graphics g, int itemX, int itemY, int square_size) {
    float percentage = (float) item.getResource().getAmount() * 1.0f / item.getResource().getMaxPileSize() * 1.0f;
    Rectangle rectangle = new Rectangle(itemX + 3, itemY + 3, square_size - 6, square_size - 6);
    render(g, item.getResource().getType(), percentage, rectangle);
  }

  private void render(Graphics g, ResourceType resourceType, float percentage, Rectangle rectangle) {
    Image image;
    switch (resourceType) {
      case WOOD:
        image = blockSheet.getSprite("wood");
        break;
      default:
        image = blockSheet.getSprite("unknown");
    }

    float x = rectangle.getCenterX() - ((rectangle.getWidth() * percentage) / 2);
    float y = rectangle.getY() + rectangle.getHeight() * (1.0f - percentage);
    float width = rectangle.getWidth() * percentage;
    float height = rectangle.getHeight() * percentage;

    g.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth(), image.getHeight());
  }
}
