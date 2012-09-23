package kniemkiewicz.jqblocks.ingame.object.workplace;

import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.action.AbstractActionController;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.controller.event.Event;
import kniemkiewicz.jqblocks.ingame.controller.event.input.keyboard.KeyPressedEvent;
import kniemkiewicz.jqblocks.ingame.object.ProgressBar;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qba
 * Date: 12.08.12
 */
@Component
public class WorkplaceActionController extends AbstractActionController {

  @Autowired
  private WorkplaceController workplaceController;

  @Autowired
  private RenderQueue renderQueue;

  private ProgressBar completionEffect;

  private int totalDuration;

  private int remainingDuration;

  @Override
  protected boolean canPerformAction(int x, int y) {
    WorkplaceDefinition workplaceDefinition = workplaceController.findWorkplace(new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK));
    if (workplaceDefinition == null) return false;
    return workplaceDefinition.canInteract();
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    WorkplaceBackgroundElement wbe = workplaceController.findWorkplaceBackgroundElement(new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK));
    return GeometryUtils.getBoundingRectangle(wbe.getShape());
  }

  @Override
  protected void startAction() {
    assert completionEffect == null;
    completionEffect = new ProgressBar(affectedRectangle);
    WorkplaceDefinition workplaceDefinition = workplaceController.findWorkplace(affectedRectangle);
    totalDuration = workplaceDefinition.getActionDuration();
    remainingDuration = totalDuration;
    renderQueue.add(completionEffect);
  }

  @Override
  protected void stopAction() {
    renderQueue.remove(completionEffect);
    completionEffect = null;
    remainingDuration = 0;
  }

  @Override
  protected void updateAction(int delta) {
    remainingDuration -= delta;
    completionEffect.setPercentage(((totalDuration - remainingDuration) * 100) / totalDuration);
  }

  @Override
  protected boolean isActionCompleted() {
    return remainingDuration <= 0;
  }

  @Override
  protected void onAction() {
    if (affectedRectangle != null) {
      WorkplaceDefinition workplaceDefinition = workplaceController.findWorkplace(affectedRectangle);
      workplaceDefinition.interact();
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
