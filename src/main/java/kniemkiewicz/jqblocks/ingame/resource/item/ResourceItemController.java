package kniemkiewicz.jqblocks.ingame.resource.item;

import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.controller.event.Event;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
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
    return new ResourceObject(item.resource, Sizes.floorToBlockSizeX(centerX), Sizes.floorToBlockSizeY(centerY));
  }
}
