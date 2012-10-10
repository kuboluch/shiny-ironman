package kniemkiewicz.jqblocks.ingame.renderer.creature;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.renderer.AnimationRenderer;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: qba
 * Date: 08.10.12
 */
public class RunningCreatureRenderer<T extends RenderableObject & RunningCreatureRenderer.RunningCreature & HasFullXYMovement> implements ObjectRenderer<T> {

  public interface RunningCreature<T extends AnimationRenderer.AnimationCompatible> extends AnimationRenderer.AnimationCompatible<T> {
    boolean isRunning();
  }

  @Autowired
  ControllerUtils utils;

  ObjectRenderer<T> standingRenderer;
  AnimationRenderer<T> walkingRenderer;
  AnimationRenderer<T> runningRenderer;

  public RunningCreatureRenderer(ObjectRenderer<T> standingRenderer, AnimationRenderer<T> walkingRenderer, AnimationRenderer<T> runningRenderer) {
    this.standingRenderer = standingRenderer;
    this.walkingRenderer = walkingRenderer;
    this.runningRenderer = runningRenderer;
  }

  @Override
  public void render(T object, Graphics g, PointOfView pov) {
    if (object.getXYMovement().getXMovement().getSpeed() == 0.0f) {
      standingRenderer.render(object, g, pov);
    } else {
      if (object.isRunning()) {
        runningRenderer.render(object, g, pov);
      } else {
        walkingRenderer.render(object, g, pov);
      }
    }
  }
}