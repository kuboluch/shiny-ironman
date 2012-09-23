package kniemkiewicz.jqblocks.ingame.content.bird;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.util.Direction;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 23.09.12
 */
public class Bird implements RenderableObject<Bird>{

  // Numbers here are just unscaled image locations in spritesheet.
  enum BirdColor {
    BROWN_PINK(0,0),
    WHITE(1,0),
    BROWN_WHITE(0,1),
    BLUE(1,1);
    final int x;
    final int y;
    private BirdColor(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }
  }

  final static float SPEED = Sizes.BLOCK / 3;
  final static float SIZE = Sizes.BLOCK * 2;

  static XYMovementDefinition BIRD_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(SPEED),
      new MovementDefinition().setMaxSpeed(SPEED)
  );

  final Rectangle rectangle;
  final BirdColor birdColor;
  final Direction direction;
  final XYMovement movement;
  long age = 0;

  public Bird(BirdColor birdColor, Direction direction, float x, float y) {
    this.birdColor = birdColor;
    this.direction = direction;
    this.movement = BIRD_MOVEMENT.getMovement(x ,y).setXSpeed(direction == Direction.RIGHT ? SPEED : - SPEED);
    this.rectangle = new Rectangle(x, y, SIZE, SIZE);
  }


  public XYMovement getXYMovement() {
    return movement;
  }

  public void update(int delta) {
    movement.update(delta);
    movement.defaultUpdateShape(rectangle);
    age += delta;
  }

  private static final BeanName<SimpleBirdRenderer> RENDERER = new BeanName<SimpleBirdRenderer>(SimpleBirdRenderer.class);

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) { }

  @Override
  public Layer getLayer() {
    return Layer.MINUS_INF;
  }

  @Override
  public Rectangle getShape() {
    return rectangle;
  }

  public BirdColor getBirdColor() {
    return birdColor;
  }

  public Direction getDirection() {
    return direction;
  }

  public long getAge() {
    return age;
  }
}
