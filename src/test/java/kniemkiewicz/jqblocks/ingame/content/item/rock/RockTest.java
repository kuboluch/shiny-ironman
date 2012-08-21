package kniemkiewicz.jqblocks.ingame.content.item.rock;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: knie
 * Date: 7/29/12
 */
public class RockTest {

  @Test
  public void doTest() {
    Rock rock = new Rock(10, 20, true);
    Rock clonedRock = (Rock) SerializationUtils.clone(rock);
    Assert.assertEquals(rock.getShape().getWidth(), clonedRock.getShape().getWidth(), 0.01);
    Assert.assertEquals(rock.getShape().getCenterX(), clonedRock.getShape().getCenterX(), 0.01);
    Assert.assertEquals(rock.getShape().getCenterY(), clonedRock.getShape().getCenterY(), 0.01);
    Assert.assertEquals(rock.large, clonedRock.large);
  }
}
