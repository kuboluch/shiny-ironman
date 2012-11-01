package kniemkiewicz.jqblocks.ingame.content.creature.rooster;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.renderer.FlippingImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.creature.RunningCreatureRenderer;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.random.GaussianDistribution;
import kniemkiewicz.jqblocks.ingame.util.random.ProbabilityDistribution;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: qba
 * Date: 08.10.12
 */
public interface RoosterDefinition {
  static int MAX_HP = 1;

  static final int WIDTH = 24;
  static final int HEIGHT = 20;

  static final int AWARENESS_RADIUS = Sizes.BLOCK * 25;

  static final float WALK_SPEED = Sizes.BLOCK * 1;
  static final float MAX_SPEED = Sizes.BLOCK * 2;
  static final float DEFAULT_X_DECELERATION = MAX_SPEED / 6.0f;

  static final ProbabilityDistribution START_MOVING = GaussianDistribution.withExpectationAndRange(2000, 1500);
  static final ProbabilityDistribution STOP_MOVING = GaussianDistribution.withExpectationAndRange(4000, 3500);
  static final ProbabilityDistribution CHANGE_DIRECTION = GaussianDistribution.withExpectationAndRange(8000, 7000);
  static final ProbabilityDistribution STOP_RUNNING_AWAY = GaussianDistribution.withExpectationAndRange(6000, 2000);

  static final XYMovementDefinition MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(MAX_SPEED).setDefaultDeceleration(DEFAULT_X_DECELERATION * 3).setAutoDirection(false),
      new MovementDefinition().setMaxSpeed(MAX_SPEED)
  );

  static final BeanName<FlippingImageRenderer> STANDING_RENDERER =
      new BeanName<FlippingImageRenderer>(FlippingImageRenderer.class, "standingRoosterRenderer");

  static final BeanName<RunningCreatureRenderer> RENDERER =
      new BeanName<RunningCreatureRenderer>(RunningCreatureRenderer.class, "roosterRenderer");

  static final BeanName<RoosterController> CONTROLLER = new BeanName<RoosterController>(RoosterController.class);

  static Vector2f STANDING_IMAGE_CENTER_SHIFT = new Vector2f(0, 3);
}
