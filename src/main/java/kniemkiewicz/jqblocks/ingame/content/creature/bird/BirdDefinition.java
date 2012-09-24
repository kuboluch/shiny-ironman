package kniemkiewicz.jqblocks.ingame.content.creature.bird;

import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.renderer.AnimationRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.SimpleImageRenderer;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 23.09.12
 */
public interface BirdDefinition {

  static int MAX_HP = 1;

  static final int WIDTH = 12;
  static final int HEIGHT = 16;

  static final float SPEED = Player.MAX_X_SPEED / 3;
  static final float ACCELERATION = SPEED;

  static XYMovementDefinition BIRD_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(SPEED),
      new MovementDefinition().setMaxSpeed(SPEED)
  );

  static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(SimpleImageRenderer.class, "birdRenderer");
  static final BeanName<AnimationRenderer> WALKING_RENDERER = new BeanName<AnimationRenderer>(AnimationRenderer.class, "birdWalkingRenderer");
  static final BeanName<AnimationRenderer> FLYING_RENDERER = new BeanName<AnimationRenderer>(AnimationRenderer.class, "birdFlyingRenderer");

  static final BeanName<BirdController> CONTROLLER = new BeanName<BirdController>(BirdController.class);

}
