package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.content.creature.SimpleBody;
import kniemkiewicz.jqblocks.ingame.controller.AgeUpdateController;
import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.renderer.AnimationRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/24/12
 */
public class ZombieBody extends SimpleBody implements AgeUpdateController.HasAge,UpdateQueue.ToBeUpdated<AgeUpdateController.HasAge>, AnimationRenderer.AnimationCompatible<SimpleBody> {

  static XYMovementDefinition BAT_BODY_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(Zombie.SPEED).setAutoDirection(false),
      new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );

  long age = 0;

  public ZombieBody(XYMovement movement) {
    super(BAT_BODY_MOVEMENT.getMovement(movement), Zombie.WIDTH);
  }

  private static final BeanName<AnimationRenderer> RENDERER = new BeanName<AnimationRenderer>(AnimationRenderer.class, "zombieBodyRenderer");

  @Override
  public BeanName<AnimationRenderer> getRenderer() {
    return RENDERER;
  }

  public boolean addTo(RenderQueue renderQueue, FreeFallController freeFallController, UpdateQueue updateQueue) {
    updateQueue.add(this);
    super.addTo(renderQueue, freeFallController);
    return true;
  }

  @Override
  public Class<AgeUpdateController> getUpdateController() {
    return AgeUpdateController.class;
  }

  public long getAge() {
    return age;
  }

  @Override
  public void setAge(long age) {
    this.age = age;
  }
}
