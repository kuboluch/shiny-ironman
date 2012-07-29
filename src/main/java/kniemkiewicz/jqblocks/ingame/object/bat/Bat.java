package kniemkiewicz.jqblocks.ingame.object.bat;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.util.LimitedSpeed;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: knie
 * Date: 7/24/12
 */
public class Bat implements RenderableObject<Bat>,UpdateQueue.ToBeUpdated<Bat>,HasHealthPoints {

  Rectangle rectangle;
  int y;
  LimitedSpeed xMovement;
  public static final float X_SPEED = Player.MAX_X_SPEED / 3;
  public static final int SIZE = 2 * Sizes.BLOCK;
  private static int BAT_BP = 5;
  HealthPoints healthPoints = new HealthPoints(BAT_BP, this);

  public Bat(int x, int y) {
    this.xMovement = new LimitedSpeed(X_SPEED, X_SPEED, x, 0);
    this.y = y;
    rectangle = new Rectangle(x, y, SIZE, SIZE);
  }

  // Do not add objects manually. Using this method makes sure you won't forget any part.
  public boolean addTo(MovingObjects movingObjects, RenderQueue renderQueue, UpdateQueue updateQueue) {
    if (!movingObjects.add(this)) return false;
    renderQueue.add(this);
    updateQueue.add(this);
    return true;
  }

  @Override
  public Class<? extends ObjectRenderer<Bat>> getRenderer() {
    return BatRenderer.class;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {  }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }

  @Override
  public Rectangle getShape() {
    return rectangle;
  }

  @Override
  public Class<? extends UpdateQueue.UpdateController<Bat>> getUpdateController() {
    return BatController.class;
  }

  @Override
  public HealthPoints getHp() {
    return healthPoints;
  }

  @Override
  public void killed(World killer) {
    killer.killMovingObject(this);
  }
}
