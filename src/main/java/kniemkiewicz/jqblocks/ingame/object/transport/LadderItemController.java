package kniemkiewicz.jqblocks.ingame.object.transport;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.item.LadderItem;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: qba
 * Date: 15.08.12
 */
@Component
public class LadderItemController implements ItemController<LadderItem> {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Override
  public void listen(LadderItem selectedItem, List<Event> events) {
    // TODO add ladder to background on mousePressedEvent
  }

  @Override
  public Shape getDropObjectShape(LadderItem item, int centerX, int centerY) {
    return new Ladder(centerX, centerY).getShape();
  }

  @Override
  public MovingPhysicalObject getDropObject(LadderItem item, int centerX, int centerY) {
    Ladder ladder = new Ladder(centerX, centerY);
    ladder.addTo(movingObjects, renderQueue);
    return ladder;
  }
}
