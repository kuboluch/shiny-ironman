package kniemkiewicz.jqblocks.ingame.content.creature.rabbit;

import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.renderer.FlippingImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 23.09.12
 */
public interface RabbitDefinition {
  static int MAX_HP = 1;

  static final int WIDTH = 32;
  static final int HEIGHT = 24;

  static final float SPEED = Player.MAX_X_SPEED / 3;

  static XYMovementDefinition RABBIT_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(SPEED),
      new MovementDefinition().setMaxSpeed(SPEED)
  );

  static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(FlippingImageRenderer.class, "rabbitRenderer");
  static final BeanName<ImageRenderer> JUMPING_RENDERER = new BeanName<ImageRenderer>(FlippingImageRenderer.class, "rabbitJumpingRenderer");
  static final BeanName<ImageRenderer> LANDING_RENDERER = new BeanName<ImageRenderer>(FlippingImageRenderer.class, "rabbitLandingRenderer");

  static final BeanName<RabbitController> CONTROLLER = new BeanName<RabbitController>(RabbitController.class);
}
