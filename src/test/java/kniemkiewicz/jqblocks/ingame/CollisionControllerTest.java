package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.content.resource.Wood;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceObject;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.geom.Rectangle;

import java.util.EnumSet;

/**
 * User: qba
 * Date: 11.08.12
 */
public class CollisionControllerTest {
  private static final EnumSet<CollisionController.ObjectType> PICKABLE =
      EnumSet.of(CollisionController.ObjectType.PICKABLE);

  @Test
  public void doTest() {
    CollisionController collisionController = new CollisionController();

    ResourceObject<Wood> woodObject = new ResourceObject<Wood>(new Wood(), 0, 100);
    collisionController.add(PICKABLE, woodObject, false);

    Rectangle test = new Rectangle(0, 100, 1, 1);
    Assert.assertTrue(collisionController.intersects(PICKABLE, test));
    Assert.assertNotNull(collisionController.fullSearch(PICKABLE, test));
    Assert.assertTrue(collisionController.fullSearch(PICKABLE, test).size() == 1);

    test.setX(ResourceObject.SIZE);
    // TODO should it intersect or not?
    Assert.assertFalse(collisionController.intersects(PICKABLE, test));
    Assert.assertNotNull(collisionController.fullSearch(PICKABLE, test));
    Assert.assertTrue(collisionController.fullSearch(PICKABLE, test).isEmpty());
  }
}
