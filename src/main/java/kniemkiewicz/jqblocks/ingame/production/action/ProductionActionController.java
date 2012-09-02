package kniemkiewicz.jqblocks.ingame.production.action;

import com.google.common.base.Optional;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.action.AbstractActionController;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.keyboard.KeyPressedEvent;
import kniemkiewicz.jqblocks.ingame.event.production.ProductionCompleteEvent;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.production.CanProduce;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignment;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignmentController;
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
public abstract class ProductionActionController extends AbstractActionController {

  @Autowired
  ProductionAssignmentController productionAssignmentController;

  @Autowired
  EventBus eventBus;

  public abstract CanProduce getProductionPlace(Rectangle rectangle);

  @Override
  protected boolean canPerformAction(int x, int y) {
    Rectangle rectangle = new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK);
    CanProduce productionPlace = getProductionPlace(rectangle);
    if (productionPlace == null) return false;
    return productionAssignmentController.hasAssigment(productionPlace);
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    Rectangle rectangle = new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK);
    CanProduce productionPlace = getProductionPlace(rectangle);
    return GeometryUtils.getBoundingRectangle(checkNotNull(productionPlace).getShape());
  }

  @Override
  protected void startAction() {
  }

  @Override
  protected void stopAction() {
  }

  @Override
  protected void updateAction(int delta) {
    CanProduce productionPlace = getProductionPlace(affectedRectangle);
    Optional<ProductionAssignment> assignment = productionAssignmentController.getActiveAssignment(checkNotNull(productionPlace));
    if (assignment.isPresent()) {
      assignment.get().update(delta);
    }
  }

  @Override
  protected boolean isActionCompleted() {
    CanProduce productionPlace = getProductionPlace(affectedRectangle);
    Optional<ProductionAssignment> assignment = productionAssignmentController.getActiveAssignment(checkNotNull(productionPlace));
    return assignment.isPresent() && assignment.get().isCompleted();
  }

  @Override
  protected void onAction() {
    if (affectedRectangle != null) {
      CanProduce productionPlace = getProductionPlace(affectedRectangle);
      Optional<ProductionAssignment> assignment = productionAssignmentController.getActiveAssignment(checkNotNull(productionPlace));
      if (assignment.isPresent() && assignment.get().isCompleted()) {
        productionAssignmentController.removeActiveAssigment(productionPlace);
        eventBus.broadcast(new ProductionCompleteEvent(productionPlace, assignment.get()));
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
        event.consume();
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
