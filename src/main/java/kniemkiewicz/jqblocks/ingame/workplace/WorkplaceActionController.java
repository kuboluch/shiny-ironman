package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.action.AbstractActionController;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.input.keyboard.KeyPressedEvent;
import kniemkiewicz.jqblocks.ingame.object.CompletionEffect;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: qba
 * Date: 12.08.12
 */
@Component
public class WorkplaceActionController extends AbstractActionController {

  @Autowired
  private Backgrounds backgrounds;

  @Autowired
  private RenderQueue renderQueue;

  private CompletionEffect completionEffect;

  private int totalDuration;

  private int remainingDuration;

  @Override
  protected boolean canPerformAction(int x, int y) {
    WorkplaceDefinition workplaceDefinition = findWorkplace(new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK));
    if (workplaceDefinition == null) return false;
    return workplaceDefinition.canInteract();
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    WorkplaceBackgroundElement wbe = findWorkplaceBackgroundElement(new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK));
    return GeometryUtils.getBoundingRectangle(wbe.getShape());
  }

  @Override
  protected void startAction() {
    assert completionEffect == null;
    completionEffect = new CompletionEffect(affectedRectangle);
    WorkplaceDefinition workplaceDefinition = findWorkplace(affectedRectangle);
    totalDuration = workplaceDefinition.getDurationToComplete();
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
      WorkplaceDefinition workplaceDefinition = findWorkplace(affectedRectangle);
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

  private WorkplaceBackgroundElement findWorkplaceBackgroundElement(Rectangle rect) {
    BackgroundElement backgroundElement = null;
    Iterator<BackgroundElement> it = backgrounds.intersects(rect);
    while (it.hasNext()) {
      backgroundElement = it.next();
      if (backgroundElement.isWorkplace()) {
        return (WorkplaceBackgroundElement) backgroundElement;
      }
    }
    return null;
  }

  private WorkplaceDefinition findWorkplace(Rectangle rect) {
    WorkplaceBackgroundElement wbe = findWorkplaceBackgroundElement(rect);
    if (wbe != null) {
      return wbe.getWorkplace();
    }
    return null;
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
