package kniemkiewicz.jqblocks.ingame.content.item.fireball;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.controller.ProjectileController;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.serialization.SerializableRef;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: krzysiek
 * Date: 17.09.12
 */
public class Fireball implements ProjectileController.Projectile<Fireball> {

  static private int DMG = 30;
  private static final long serialVersionUID = 1;

  static final float SPEED = Player.MAX_X_SPEED * 1.5f;
  private static final float SIZE = Sizes.BLOCK;

  private static XYMovementDefinition ARROW_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition(), new MovementDefinition());

  final XYMovement movement;
  final private SerializableRef<QuadTree.HasShape> source;
  private Rectangle rectangle;

  public Fireball(float x, float y, QuadTree.HasShape source, float xSpeed, float ySpeed) {
    this.movement = ARROW_MOVEMENT.getMovement(x - SIZE / 2, y - SIZE / 2).setXSpeed(xSpeed).setYSpeed(ySpeed);
    this.source = new SerializableRef<QuadTree.HasShape>(source);
    this.rectangle = new Rectangle(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
  }

  private static final BeanName<FireballRenderer> RENDERER = new BeanName<FireballRenderer>(FireballRenderer.class);

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) { }

  @Override
  public Shape getShape() {
    return rectangle;
  }

  @Override
  public Layer getLayer() {
    return Layer.ARROWS;
  }


  public void update(int delta) {
    movement.update(delta);
    rectangle.setX(movement.getX());
    rectangle.setY(movement.getY());
  }

  @Override
  public boolean hitWall(World world) {
    FireballExplosion explosion = new FireballExplosion(rectangle.getCenterX(), rectangle.getCenterY());
    explosion.addTo(world.getRenderQueue(), world.getUpdateQueue());
    return false;
  }

  @Override
  public void hitTarget(KillablePhysicalObject kpo, World world) {
    kpo.getHp().damage(DMG, this, world);
  }

  public XYMovement getMovement() {
    return movement;
  }

  public QuadTree.HasShape getSource() {
    return source.get();
  }

  @Override
  public Class<ProjectileController> getUpdateController() {
    return ProjectileController.class;
  }
}

