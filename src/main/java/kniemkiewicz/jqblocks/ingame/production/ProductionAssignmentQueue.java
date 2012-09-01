package kniemkiewicz.jqblocks.ingame.production;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qba
 * Date: 01.09.12
 */
public class ProductionAssignmentQueue {

  static final int MAX_QUEUE_SIZE = 5;

  List<ProductionAssignment> assignmentQueue = new ArrayList<ProductionAssignment>();

  public boolean hasAssigment() {
    return !assignmentQueue.isEmpty();
  }

  public boolean canAssign() {
    return assignmentQueue.size() < MAX_QUEUE_SIZE;
  }

  public List<ProductionAssignment> getAssignmentQueue() {
    return ImmutableList.copyOf(assignmentQueue);
  }

  public Optional<ProductionAssignment> getActiveAssigment() {
    if (assignmentQueue.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(assignmentQueue.get(0));
  }

  public void add(ProductionAssignment assignment) {
    if (!canAssign()) return;
    assignmentQueue.add(assignment);
  }

  public void remove(ProductionAssignment assignment) {
    assignmentQueue.remove(assignment);
  }
}
