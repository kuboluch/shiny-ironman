package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.content.block.dirt.NaturalDirtBackground;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Iterator;

/**
 * User: qba
 * Date: 13.08.12
 */
public class BackgroundsTest {

  @Autowired
  Backgrounds backgrounds;

  @Test
  public void topIntersectTest() {
    Backgrounds backgrounds = new Backgrounds(Collections.singleton((BackgroundElement) new NaturalDirtBackground(0f, 96f, 48f, 96f)));
    Iterator<BackgroundElement> iter = backgrounds.intersects(new Rectangle(0f, 0f, 48f, 96f));
    Assert.assertFalse(iter.hasNext());

    Iterator<BackgroundElement> iter2 = backgrounds.intersects(new Rectangle(0f, 1f, 48f, 96f));
    Assert.assertTrue(iter2.hasNext());

    Iterator<BackgroundElement> iter3 = backgrounds.intersects(new Rectangle(0f, -1f, 48f, 96f));
    Assert.assertFalse(iter3.hasNext());
  }

  @Test
  public void bottomIntersectTest() {
    Backgrounds backgrounds = new Backgrounds(Collections.singleton((BackgroundElement) new NaturalDirtBackground(0f, 0f, 48f, 96f)));
    Iterator<BackgroundElement> iter = backgrounds.intersects(new Rectangle(0f, 96f, 48f, 96f));
    Assert.assertFalse(iter.hasNext());

    Iterator<BackgroundElement> iter2 = backgrounds.intersects(new Rectangle(0f, 97f, 48f, 96f));
    Assert.assertFalse(iter2.hasNext());

    Iterator<BackgroundElement> iter3 = backgrounds.intersects(new Rectangle(0f, 95f, 48f, 96f));
    Assert.assertTrue(iter3.hasNext());
  }

  @Test
  public void leftIntersectTest() {
    Backgrounds backgrounds = new Backgrounds(Collections.singleton((BackgroundElement) new NaturalDirtBackground(48f, 0f, 48f, 96f)));
    Iterator<BackgroundElement> iter = backgrounds.intersects(new Rectangle(0f, 0f, 48f, 96f));
    Assert.assertFalse(iter.hasNext());

    Iterator<BackgroundElement> iter2 = backgrounds.intersects(new Rectangle(1f, 0f, 48f, 96f));
    Assert.assertTrue(iter2.hasNext());

    Iterator<BackgroundElement> iter3 = backgrounds.intersects(new Rectangle(-1f, 0f, 48f, 96f));
    Assert.assertFalse(iter3.hasNext());
  }

  @Test
  public void rightIntersectTest() {
    Backgrounds backgrounds = new Backgrounds(Collections.singleton((BackgroundElement) new NaturalDirtBackground(0f, 0f, 48f, 96f)));
    Iterator<BackgroundElement> iter = backgrounds.intersects(new Rectangle(48f, 0f, 48f, 96f));
    Assert.assertFalse(iter.hasNext());

    Iterator<BackgroundElement> iter2 = backgrounds.intersects(new Rectangle(49f, 0f, 48f, 96f));
    Assert.assertFalse(iter2.hasNext());

    Iterator<BackgroundElement> iter3 = backgrounds.intersects(new Rectangle(47f, 0f, 48f, 96f));
    Assert.assertTrue(iter3.hasNext());
  }
}
