package kniemkiewicz.jqblocks.ingame.event.production;

import kniemkiewicz.jqblocks.ingame.event.AbstractEvent;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignment;

/**
 * User: qba
 * Date: 01.09.12
 */
public class ProductionCompleteEvent extends AbstractEvent {

  WorkplaceBackgroundElement workplace;

  ProductionAssignment assignment;

  public ProductionCompleteEvent(WorkplaceBackgroundElement workplace, ProductionAssignment assignment) {
    this.assignment = assignment;
  }

  public WorkplaceBackgroundElement getWorkplace() {
    return workplace;
  }

  public ProductionAssignment getAssignment() {
    return assignment;
  }
}
