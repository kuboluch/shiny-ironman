package kniemkiewicz.jqblocks.ingame.workplace;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qba
 * Date: 05.08.12
 */
public class WorkplaceController {

  List<Workplace> workplaces = new ArrayList<Workplace>();

  public WorkplaceController(List<Workplace> workplaces) {
    this.workplaces = workplaces;
  }

  public List<Workplace> getWorkplaces() {
    return new ArrayList<Workplace>(workplaces);
  }
}
