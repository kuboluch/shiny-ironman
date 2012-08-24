package kniemkiewicz.jqblocks.ingame.content.item.rock;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: knie
 * Date: 7/27/12
 */
@Component
public class RockItemController implements ItemController<RockItem> {

  @Override
  public void listen(RockItem selectedItem, List<Event> events) { }

  @Override
  public DroppableObject getObject(RockItem item, int centerX, int centerY) {
    int maxY = centerY - (item.isLarge() ? Rock.LARGE_CIRCLE_RADIUS : Rock.SMALL_CIRCLE_RADIUS);
    return new Rock(centerX, maxY, item.isLarge());
  }
}
