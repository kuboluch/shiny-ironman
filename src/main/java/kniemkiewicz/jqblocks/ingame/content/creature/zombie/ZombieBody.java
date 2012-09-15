package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.content.creature.SimpleBody;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.AnimationRenderer;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/24/12
 */
public class ZombieBody extends SimpleBody implements UpdateQueue.ToBeUpdated<ZombieBody>, AnimationRenderer.AnimationCompatible<SimpleBody> {

  static XYMovementDefinition BAT_BODY_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(Zombie.SPEED).setAutoDirection(false),
      new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );

  int age = 0;

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
  public Class<? extends UpdateQueue.UpdateController<ZombieBody>> getUpdateController() {
    return ZombieBodyController.class;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
