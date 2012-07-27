package kniemkiewicz.jqblocks.ingame.object.rock;

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
public class RockItemController implements ItemController<RockItem> {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Override
  public void listen(RockItem selectedItem, List<Event> events) { }

  @Override
  public MovingPhysicalObject getDropObject(RockItem item, int centerX, int centerY) {
    int maxY = centerY - (item.isLarge() ? Rock.LARGE_CIRCLE_RADIUS : Rock.SMALL_CIRCLE_RADIUS);
    Rock rock = new Rock(centerX, maxY, item.isLarge());
    rock.addTo(renderQueue, movingObjects);
    return rock;
  }
}
