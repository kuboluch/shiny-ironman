package kniemkiewicz.jqblocks.ingame.content.creature.rabbit;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.renderer.FlippingImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.creature.JumpingCreatureRenderer;
import kniemkiewicz.jqblocks.ingame.util.random.GaussianDistribution;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.random.LogNormalDistribution;
import kniemkiewicz.jqblocks.ingame.util.random.ProbabilityDistribution;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: qba
 * Date: 23.09.12
 */
public interface RabbitDefinition {
  static int MAX_HP = 1;

  static final int WIDTH = 24;
  static final int HEIGHT = 20;

  static final int CAUTIOUS_DISTANCE = Sizes.BLOCK * 25;
  static final int DANGER_DISTANCE = Sizes.BLOCK * 15;
  static final int AWARENESS_RADIUS = Sizes.BLOCK * 25;

  static final float JUMP_SPEED_X = Player.JUMP_SPEED * 1.0f;
  static final float JUMP_SPEED_Y = Player.JUMP_SPEED * 1.2f;

  static final float RABBIT_SLOWNESS = 1.3f;
  static final float MAX_SPEED = Sizes.BLOCK * 4;
  static final float DEFAULT_X_DECELERATION = MAX_SPEED / 6.0f;

  static final ProbabilityDistribution MOVING_JUMPS = GaussianDistribution.withExpectationAndRange(1500, 500);
  static final ProbabilityDistribution RUNNING_JUMPS = GaussianDistribution.withExpectationAndRange(100, 10);
  static final ProbabilityDistribution START_MOVING = GaussianDistribution.withExpectationAndRange(4000, 2000);
  static final ProbabilityDistribution STOP_MOVING = GaussianDistribution.withExpectationAndRange(8000, 4000);
  static final ProbabilityDistribution CHANGE_DIRECTION = GaussianDistribution.withExpectationAndRange(16000, 8000);
  static final ProbabilityDistribution REACTION_TIME = LogNormalDistribution.withExpectationAndMinimum(300, 100);
  static final ProbabilityDistribution STOP_RUNNING_AWAY = GaussianDistribution.withExpectationAndRange(6000, 2000);

  static final XYMovementDefinition MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(MAX_SPEED).setDefaultDeceleration(DEFAULT_X_DECELERATION),
      new MovementDefinition().setMaxSpeed(MAX_SPEED)
  );

  static final BeanName<FlippingImageRenderer> STANDING_RENDERER =
      new BeanName<FlippingImageRenderer>(FlippingImageRenderer.class, "standingRabbitRenderer");

  static final BeanName<JumpingCreatureRenderer> RENDERER =
      new BeanName<JumpingCreatureRenderer>(JumpingCreatureRenderer.class, "rabbitRenderer");

  static final BeanName<RabbitController> CONTROLLER = new BeanName<RabbitController>(RabbitController.class);

  static Vector2f STANDING_IMAGE_CENTER_SHIFT = new Vector2f(0, 5);
}
