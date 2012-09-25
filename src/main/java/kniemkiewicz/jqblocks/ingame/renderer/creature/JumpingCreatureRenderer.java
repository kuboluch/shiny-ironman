package kniemkiewicz.jqblocks.ingame.renderer.creature;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: qba
 * Date: 24.09.12
 */
public class JumpingCreatureRenderer<T extends RenderableObject & HasFullXYMovement> implements ObjectRenderer<T> {

  @Autowired
  ControllerUtils utils;

  ObjectRenderer<T> standingRenderer;
  ObjectRenderer<T> jumpingRenderer;
  ObjectRenderer<T> landingRenderer;

  public JumpingCreatureRenderer(ObjectRenderer<T> standingRenderer, ObjectRenderer<T> jumpingRenderer, ObjectRenderer<T> landingRenderer) {
    this.standingRenderer = standingRenderer;
    this.jumpingRenderer = jumpingRenderer;
    this.landingRenderer = landingRenderer;
  }

  @Override
  public void render(T object, Graphics g, PointOfView pov) {
    boolean flying = utils.isFlying(object.getShape());
    if (!flying) {
      standingRenderer.render(object, g, pov);
    } else if (object.getXYMovement().getYMovement().getSpeed() <= 0.0f) {
      jumpingRenderer.render(object, g, pov);
    } else {
      landingRenderer.render(object, g, pov);
    }
  }
}
