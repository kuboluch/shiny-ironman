package kniemkiewicz.jqblocks.ingame.controller;

import junit.framework.Assert;
import org.junit.Test;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 18.07.12
 */
public class HitResolverTest {
  @Test
  public void testDecidePlayerWalkingTopFromCorner(){
    float dx = 0.25f;
    float dy = 0.47f;
    Rectangle player = new Rectangle(dx, 1444 + dy, 19, 39);
    Rectangle block = new Rectangle(0, 1483, 39, 39);
    Assert.assertEquals(HitResolver.Decision.TOP, HitResolver.decide(player, dx, dy, block));
    dx = - dx;
    player = new Rectangle(dx, 1444 + dy, 19, 39);
    Assert.assertEquals(HitResolver.Decision.TOP, HitResolver.decide(player, dx, dy, block));
  }
  @Test
  public void testDecidePlayerWalkingTop(){
    float dx = 0.25f;
    float dy = 0.47f;
    Rectangle player = new Rectangle(dx + 0.5f, 1444 + dy, 19, 39);
    Rectangle block = new Rectangle(0, 1483, 39, 39);
    Assert.assertEquals(HitResolver.Decision.TOP, HitResolver.decide(player, dx, dy, block));
  }
}
