package kniemkiewicz.jqblocks.ingame.content.block.levelWall;

import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * Those wall are invisible and make sure that player won't get close to the end of level.
 * User: knie
 * Date: 8/27/12
 */
public class LevelWall implements PhysicalObject{

  final Rectangle shape;

  public LevelWall(Rectangle shape) {
    this.shape = shape;
  }

  @Override
  public Shape getShape() {
    return shape;
  }
}
