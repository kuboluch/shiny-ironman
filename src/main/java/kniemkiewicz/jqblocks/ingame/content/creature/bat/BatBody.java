package kniemkiewicz.jqblocks.ingame.content.creature.bat;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.content.creature.SimpleBody;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/24/12
 */
public class BatBody extends SimpleBody {

  public static final float X_SPEED = Bat.SPEED;
  public static final float Y_INITIAL_SPEED = Player.JUMP_SPEED / 4;

  static XYMovementDefinition BAT_BODY_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(X_SPEED),
      new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );

  public BatBody(float x, float y, int direction) {
    super(BAT_BODY_MOVEMENT.getMovement(x, y).setXSpeed(direction * X_SPEED).setYSpeed(- Y_INITIAL_SPEED), Bat.SIZE);
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRendererImpl.class, "batBodyRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  // Make addTo public
  public boolean addTo(RenderQueue renderQueue, FreeFallController freeFallController) {
    return super.addTo(renderQueue, freeFallController);
  }
}
