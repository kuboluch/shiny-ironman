package kniemkiewicz.jqblocks.ingame.production;

import com.google.common.base.Optional;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.production.ProductionCompleteEvent;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * User: qba
 * Date: 01.09.12
 */
@Component
public class ProductionAssignmentController {

  @Autowired
  EventBus eventBus;

  Map<WorkplaceBackgroundElement, ProductionAssignmentQueue> assignmentsMap = new HashMap<WorkplaceBackgroundElement, ProductionAssignmentQueue>();

  public boolean hasAssigment(WorkplaceBackgroundElement workplace) {
    if (!assignmentsMap.containsKey(workplace)) {
      return false;
    }
    return assignmentsMap.get(workplace).hasAssigment();
  }

  public boolean canAssign(WorkplaceBackgroundElement workplace) {
    if (!assignmentsMap.containsKey(workplace)) {
      return true;
    }
    return assignmentsMap.get(workplace).canAssign();
  }

  public void assign(WorkplaceBackgroundElement workplace, ProductionAssignment assignment) {
    if (!assignmentsMap.containsKey(workplace)) {
      assignmentsMap.put(workplace, new ProductionAssignmentQueue());
    }
    assignmentsMap.get(workplace).add(assignment);
  }

  public Optional<ProductionAssignment> getActiveAssignment(WorkplaceBackgroundElement workplace) {
    if (!assignmentsMap.containsKey(workplace)) {
      return Optional.absent();
    }
    return assignmentsMap.get(workplace).getActiveAssigment();
  }

  public Optional<ProductionAssignmentQueue> getAssigments(WorkplaceBackgroundElement workplace) {
    if (!assignmentsMap.containsKey(workplace)) {
      return Optional.absent();
    }
    return Optional.of(assignmentsMap.get(workplace));
  }

  public void removeActiveAssigment(WorkplaceBackgroundElement workplace) {
    if (!assignmentsMap.containsKey(workplace)) {
      return;
    }
    ProductionAssignmentQueue assignmentQueue = assignmentsMap.get(workplace);
    assignmentQueue.remove(assignmentQueue.getActiveAssigment().get());
  }

  public void update() {
    for (Map.Entry<WorkplaceBackgroundElement, ProductionAssignmentQueue> entry : assignmentsMap.entrySet()) {
      for (ProductionAssignment assignment : entry.getValue().getAssignmentQueue()) {
        if (assignment.isCompleted()) {
          entry.getValue().remove(assignment);
          eventBus.broadcast(new ProductionCompleteEvent(entry.getKey(), assignment));
        }
      }
    }
  }
}
