package kniemkiewicz.jqblocks.ingame.resource.item;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.resource.ResourceObject;
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
  public MovingPhysicalObject getDropObject(ResourceItem item, int x, int y) {
    ResourceObject ob = new ResourceObject(item.resource, Sizes.roundToBlockSizeX(x), Sizes.roundToBlockSizeY(y));
    ob.addTo(renderQueue, movingObjects);
    return ob;
  }
}
