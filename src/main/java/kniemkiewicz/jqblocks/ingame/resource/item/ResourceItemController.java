package kniemkiewicz.jqblocks.ingame.resource.item;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.resource.ResourceObject;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: knie
 * Date: 7/27/12
 */
@Component
public class ResourceItemController implements ItemController<ResourceItem> {

  @Autowired
  private RenderQueue renderQueue;

  @Autowired
  private MovingObjects movingObjects;

  @Override
  public void listen(ResourceItem selectedItem, List<Event> events) { }

  public Shape getDropObjectShape(ResourceItem item, int centerX, int centerY) {
    return ResourceObject.getShape(centerX, centerY);
  }

  @Override
  public MovingPhysicalObject getDropObject(ResourceItem item, int centerX, int centerY) {
    ResourceObject ob = new ResourceObject(item.resource, Sizes.roundToBlockSizeX(centerX), Sizes.roundToBlockSizeY(centerY));
    ob.addTo(renderQueue, movingObjects);
    return ob;
  }
}
