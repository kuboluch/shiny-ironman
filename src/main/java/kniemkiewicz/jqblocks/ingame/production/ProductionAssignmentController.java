package kniemkiewicz.jqblocks.ingame.production;

import com.google.common.base.Optional;
import kniemkiewicz.jqblocks.ingame.controller.event.EventBus;
import kniemkiewicz.jqblocks.ingame.controller.event.production.ProductionCompleteEvent;
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

  Map<CanProduce, ProductionAssignmentQueue> assignmentsMap = new HashMap<CanProduce, ProductionAssignmentQueue>();

  public boolean hasAssigment(CanProduce source) {
    if (!assignmentsMap.containsKey(source)) {
      return false;
    }
    return assignmentsMap.get(source).hasAssigment();
  }

  public boolean canAssign(CanProduce source) {
    if (!assignmentsMap.containsKey(source)) {
      return true;
    }
    return assignmentsMap.get(source).canAssign();
  }

  public void assign(CanProduce source, ProductionAssignment assignment) {
    if (!assignmentsMap.containsKey(source)) {
      assignmentsMap.put(source, new ProductionAssignmentQueue());
    }
    assignmentsMap.get(source).add(assignment);
  }

  public Optional<ProductionAssignment> getActiveAssignment(CanProduce source) {
    if (!assignmentsMap.containsKey(source)) {
      return Optional.absent();
    }
    return assignmentsMap.get(source).getActiveAssigment();
  }

  public Optional<ProductionAssignmentQueue> getAssigments(CanProduce source) {
    if (!assignmentsMap.containsKey(source)) {
      return Optional.absent();
    }
    return Optional.of(assignmentsMap.get(source));
  }

  public void removeActiveAssigment(CanProduce source) {
    if (!assignmentsMap.containsKey(source)) {
      return;
    }
    ProductionAssignmentQueue assignmentQueue = assignmentsMap.get(source);
    assignmentQueue.remove(assignmentQueue.getActiveAssigment().get());
  }

  public void update() {
    for (Map.Entry<CanProduce, ProductionAssignmentQueue> entry : assignmentsMap.entrySet()) {
      for (ProductionAssignment assignment : entry.getValue().getAssignmentQueue()) {
        if (assignment.isCompleted()) {
          entry.getValue().remove(assignment);
          eventBus.broadcast(new ProductionCompleteEvent(entry.getKey(), assignment));
        }
      }
    }
  }
}
