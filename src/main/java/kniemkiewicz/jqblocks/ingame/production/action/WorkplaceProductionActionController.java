package kniemkiewicz.jqblocks.ingame.production.action;

import kniemkiewicz.jqblocks.ingame.production.CanProduce;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 02.09.12
 */
@Component
public class WorkplaceProductionActionController extends ProductionActionController {

  @Autowired
  WorkplaceController workplaceController;

  @Override
  public CanProduce getProductionPlace(Rectangle rectangle) {
    return workplaceController.findWorkplaceBackgroundElement(rectangle);
  }
}
