package kniemkiewicz.jqblocks.ingame.content.creature.rabbit;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.renderer.creature.JumpingCreatureRenderer;
import kniemkiewicz.jqblocks.ingame.util.closure.GaussianDistribution;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 23.09.12
 */
public interface RabbitDefinition {
  static int MAX_HP = 1;

  static final int WIDTH = 16;
  static final int HEIGHT = 12;

  static final int CAUTIOUS_DISTANCE = Sizes.BLOCK * 25;
  static final int DANGER_DISTANCE = Sizes.BLOCK * 15;

  static final float JUMP_SPEED_X = Player.JUMP_SPEED * 1.0f;
  static final float JUMP_SPEED_Y = Player.JUMP_SPEED * 1.2f;

  static final float MAX_SPEED = Player.MAX_X_SPEED;
  static final float DEFAULT_X_DECELERATION = MAX_SPEED / 6.0f;

  static final GaussianDistribution MOVING_JUMPS = GaussianDistribution.withExpectationAndVariance(1500, 500);
  static final GaussianDistribution RUNNING_JUMPS = GaussianDistribution.withExpectationAndVariance(100, 10);
  static final GaussianDistribution START_MOVING = GaussianDistribution.withExpectationAndVariance(4000, 2000);
  static final GaussianDistribution STOP_MOVING = GaussianDistribution.withExpectationAndVariance(8000, 4000);
  static final GaussianDistribution CHANGE_DIRECTION = GaussianDistribution.withExpectationAndVariance(16000, 8000);

  static final XYMovementDefinition RABBIT_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(MAX_SPEED).setDefaultDeceleration(DEFAULT_X_DECELERATION),
      new MovementDefinition().setMaxSpeed(MAX_SPEED)
  );

  static final BeanName<JumpingCreatureRenderer> RENDERER =
      new BeanName<JumpingCreatureRenderer>(JumpingCreatureRenderer.class, "rabbitRenderer");

  static final BeanName<RabbitController> CONTROLLER = new BeanName<RabbitController>(RabbitController.class);
}
