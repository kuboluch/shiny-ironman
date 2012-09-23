package kniemkiewicz.jqblocks.ingame.content.creature.bird;

import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.ai.AIUtils;
import kniemkiewicz.jqblocks.ingame.content.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qba
 * Date: 22.09.12
 */
@Component
public class BirdController implements UpdateQueue.UpdateController<Bird>, HealthController<Bird>  {
  @Autowired
  World world;

  @Autowired
  ControllerUtils utils;

  @Autowired
  AIUtils aiUtils;

  @Override
  public void killed(Bird bird, QuadTree.HasShape source) {
    world.killMovingObject(bird);
  }

  @Override
  public void damaged(Bird bird, QuadTree.HasShape source, int amount) {
  }

  @Override
  public void update(Bird bird, int delta) {
    List<PhysicalObject> collisions = new ArrayList<PhysicalObject>();
    aiUtils.horizontalPatrol2(bird, delta, collisions);
    bird.setAge(bird.getAge() + delta);
  }
}
