package kniemkiewicz.jqblocks.ingame.object.peon;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.TwoFacedImageRenderer;
import kniemkiewicz.jqblocks.ingame.object.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.util.HorizontalMovement;
import kniemkiewicz.jqblocks.ingame.util.SingleAxisMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
public class Peon implements PhysicalObject,HasHealthPoints<Peon>, TwoFacedImageRenderer.Renderable{

  private static final int PEON_HP = 100;
  private static final float MAX_PEON_SPEED = Player.MAX_X_SPEED / 2;

  public static final float WIDTH = Sizes.BLOCK * 2 + 5;
  public static final float HEIGHT = Sizes.BLOCK * 3 + 5;

  HealthPoints healthPoints = new HealthPoints(PEON_HP, this);
  final Rectangle shape;

  HorizontalMovement movement;

  public static Peon createAndRegister(int x, int y, PeonController controller) {
    Peon peon = new Peon(x, y);
    if (!controller.register(peon)) return null;
    return peon;
  }

  private Peon(int x, int y) {
    movement = new HorizontalMovement(x, y, MAX_PEON_SPEED);
    shape = new Rectangle(x, y, WIDTH, HEIGHT);
  }

  @Override
  public HealthPoints getHp() {
    return healthPoints;
  }

  private static final BeanName<PeonController> CONTROLLER = new BeanName<PeonController>(PeonController.class);

  @Override
  public BeanName<? extends HealthController<Peon>> getHealthController() {
    return CONTROLLER;
  }

  @Override
  public boolean isLeftFaced() {
    return !movement.getXMovement().getLastDirection();
  }

  private static final BeanName<TwoFacedImageRenderer> RENDERER = new BeanName<TwoFacedImageRenderer>(TwoFacedImageRenderer.class, "peonRenderer");

  @Override
  public BeanName<? extends ObjectRenderer<? super TwoFacedImageRenderer.Renderable>> getRenderer() {
    return RENDERER;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) { }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }

  @Override
  public Shape getShape() {
    return shape;
  }
}
