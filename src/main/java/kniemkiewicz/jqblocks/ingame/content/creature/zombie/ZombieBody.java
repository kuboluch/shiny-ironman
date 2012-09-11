package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.content.creature.SimpleBody;
import kniemkiewicz.jqblocks.ingame.content.creature.bat.Bat;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/24/12
 */
public class ZombieBody extends SimpleBody implements UpdateQueue.ToBeUpdated<ZombieBody> {

  static XYMovementDefinition BAT_BODY_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(Zombie.SPEED),
      new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );

  int age = 0;

  public ZombieBody(XYMovement movement) {
    super(BAT_BODY_MOVEMENT.getMovement(movement));
  }

  private static final BeanName<ZombieBodyRenderer> RENDERER = new BeanName<ZombieBodyRenderer>(ZombieBodyRenderer.class);

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
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
