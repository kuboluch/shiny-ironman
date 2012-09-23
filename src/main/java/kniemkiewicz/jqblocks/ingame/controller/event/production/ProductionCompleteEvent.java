package kniemkiewicz.jqblocks.ingame.controller.event.production;

import kniemkiewicz.jqblocks.ingame.controller.event.AbstractEvent;
import kniemkiewicz.jqblocks.ingame.production.CanProduce;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignment;

/**
 * User: qba
 * Date: 01.09.12
 */
public class ProductionCompleteEvent extends AbstractEvent {
  CanProduce source;
  ProductionAssignment assignment;

  public ProductionCompleteEvent(CanProduce source, ProductionAssignment assignment) {
    this.source = source;
    this.assignment = assignment;
  }

  public CanProduce getSource() {
    return source;
  }

  public ProductionAssignment getAssignment() {
    return assignment;
  }
}
