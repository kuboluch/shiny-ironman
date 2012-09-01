package kniemkiewicz.jqblocks.ingame.production;

import com.google.common.base.Optional;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.action.AbstractActionController;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.keyboard.KeyPressedEvent;
import kniemkiewicz.jqblocks.ingame.event.production.ProductionCompleteEvent;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: qba
 * Date: 01.09.12
 */
@Component
public class ProductionActionController extends AbstractActionController {

  @Autowired
  WorkplaceController workplaceController;

  @Autowired
  ProductionAssignmentController productionAssignmentController;

  @Autowired
  EventBus eventBus;

  @Override
  protected boolean canPerformAction(int x, int y) {
    WorkplaceBackgroundElement wbe = workplaceController.findWorkplaceBackgroundElement(new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK));
    if (wbe == null) return false;
    return productionAssignmentController.hasAssigment(wbe);
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    WorkplaceBackgroundElement wbe = workplaceController.findWorkplaceBackgroundElement(new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK));
    return GeometryUtils.getBoundingRectangle(wbe.getShape());
  }

  @Override
  protected void startAction() {
  }

  @Override
  protected void stopAction() {
  }

  @Override
  protected void updateAction(int delta) {
    WorkplaceBackgroundElement wbe = workplaceController.findWorkplaceBackgroundElement(affectedRectangle);
    Optional<ProductionAssignment> assignment = productionAssignmentController.getActiveAssignment(checkNotNull(wbe));
    if (assignment.isPresent()) {
      assignment.get().update(delta);
    }
  }

  @Override
  protected boolean isActionCompleted() {
    WorkplaceBackgroundElement wbe = workplaceController.findWorkplaceBackgroundElement(affectedRectangle);
    Optional<ProductionAssignment> assignment = productionAssignmentController.getActiveAssignment(checkNotNull(wbe));
    return assignment.isPresent() && assignment.get().isCompleted();
  }

  @Override
  protected void onAction() {
    if (affectedRectangle != null) {
      WorkplaceBackgroundElement wbe = workplaceController.findWorkplaceBackgroundElement(affectedRectangle);
      Optional<ProductionAssignment> assignment = productionAssignmentController.getActiveAssignment(checkNotNull(wbe));
      if (assignment.isPresent() && assignment.get().isCompleted()) {
        productionAssignmentController.removeActiveAssigment(wbe);
        eventBus.broadcast(new ProductionCompleteEvent(wbe, assignment.get()));
      }
    }
  }

  @Override
  public void listen(List<Event> events) {
    List<KeyPressedEvent> keyPressedEvents = Collections3.collect(events, KeyPressedEvent.class);
    if (!keyPressedEvents.isEmpty()) {
      for (KeyPressedEvent e : keyPressedEvents) {
        handleKeyPressedEvent(e);
      }
    }

    super.listen(events);
  }

  private void handleKeyPressedEvent(KeyPressedEvent event) {
    if (KeyboardUtils.isInteractKey(event.getKey())) {
      int playerX = Sizes.roundToBlockSizeX(playerController.getPlayer().getShape().getCenterX());
      int playerY = Sizes.roundToBlockSizeY(playerController.getPlayer().getShape().getCenterY());

      if (affectedRectangle != null) {
        Rectangle rect = new Rectangle(playerX, playerY, 1, 1);
        if (affectedRectangle != null && (!affectedRectangle.intersects(rect))) {
          stopAction();
          affectedRectangle = null;
        }
      }
      if (affectedRectangle == null && canPerformAction(playerX, playerY)) {
        affectedRectangle = getAffectedRectangle(playerX, playerY);
        startAction();
        event.consume();
      }
    }
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    List<Class> eventTypesOfInterest = new ArrayList<Class>(super.getEventTypesOfInterest());
    if (!eventTypesOfInterest.contains(KeyPressedEvent.class)) {
      eventTypesOfInterest.add(KeyPressedEvent.class);
    }
    return eventTypesOfInterest;
  }
}
