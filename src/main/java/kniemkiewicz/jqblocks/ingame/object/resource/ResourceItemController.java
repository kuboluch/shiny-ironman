package kniemkiewicz.jqblocks.ingame.object.resource;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
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

  @Override
  public MovingPhysicalObject getDropObject(ResourceItem item, int centerX, int centerY) {
    ResourceObject ob = new ResourceObject(item.resource, centerX - ResourceObject.SIZE / 2, centerY - ResourceObject.SIZE / 2);
    ob.addTo(renderQueue, movingObjects);
    return ob;
  }
}
