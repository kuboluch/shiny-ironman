package kniemkiewicz.jqblocks.ingame.resource.item;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: knie
 * Date: 7/27/12
 */
@Component
public class ResourceItemController implements ItemController<SimpleResourceItem> {

  @Autowired
  private RenderQueue renderQueue;

  @Autowired
  private MovingObjects movingObjects;

  @Override
  public void listen(SimpleResourceItem selectedItem, List<Event> events) { }

  @Override
  public DroppableObject getObject(SimpleResourceItem item, int centerX, int centerY) {
    return new ResourceObject(item.resource, Sizes.roundToBlockSizeX(centerX), Sizes.roundToBlockSizeY(centerY));
  }
}
