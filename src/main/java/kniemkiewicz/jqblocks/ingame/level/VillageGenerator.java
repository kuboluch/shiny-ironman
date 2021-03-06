package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.BackgroundBlockType;
import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.content.creature.FlipImageBody;
import kniemkiewicz.jqblocks.ingame.content.creature.bird.Bird;
import kniemkiewicz.jqblocks.ingame.content.creature.peon.Peon;
import kniemkiewicz.jqblocks.ingame.content.creature.peon.PeonController;
import kniemkiewicz.jqblocks.ingame.content.creature.rabbit.Rabbit;
import kniemkiewicz.jqblocks.ingame.content.creature.rabbit.RabbitController;
import kniemkiewicz.jqblocks.ingame.content.creature.rabbit.RabbitDefinition;
import kniemkiewicz.jqblocks.ingame.content.creature.rooster.Rooster;
import kniemkiewicz.jqblocks.ingame.content.creature.rooster.RoosterDefinition;
import kniemkiewicz.jqblocks.ingame.content.creature.zombie.Zombie;
import kniemkiewicz.jqblocks.ingame.content.item.rock.Rock;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderBackground;
import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.controller.TimeController;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ai.paths.GraphController;
import kniemkiewicz.jqblocks.ingame.controller.ai.paths.GraphGenerator;
import kniemkiewicz.jqblocks.ingame.controller.ai.paths.PathGraph;
import kniemkiewicz.jqblocks.ingame.controller.ai.paths.Position;
import kniemkiewicz.jqblocks.ingame.object.DebugRenderableShape;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.content.background.Portal;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.object.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.ingame.object.workplace.WorkplaceDefinition;
import kniemkiewicz.jqblocks.util.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: knie
 * Date: 7/30/12
 */
@Component
public class VillageGenerator {

  public static Log logger = LogFactory.getLog(VillageGenerator.class);

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  Backgrounds backgrounds;

  @Autowired
  WorkplaceController workplaceController;

  @Resource
  WorkplaceDefinition fireplace;

  @Resource
  WorkplaceDefinition sawmill;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  PeonController peonController;

  @Autowired
  FreeFallController freeFallController;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  GraphController graphController;

  @Autowired
  RandomCaveGenerator caveGenerator;

  public static final int STARTING_X = (Sizes.MIN_X + Sizes.MAX_X) / 2;

  public static final int VILLAGE_RADIUS = 24;

  int startingY = 0;

  public int getStartingY() {
    assert startingY != 0;
    return startingY;
  }

  void makeHouse(int x, int y) {
    solidBlocks.getBackground().setRectUnscaled(new Rectangle(x - Sizes.BLOCK * 3, y  - Sizes.BLOCK * 4, Sizes.BLOCK * 6, Sizes.BLOCK * 4), BackgroundBlockType.DIRT);
    solidBlocks.add(new Rectangle(x - Sizes.BLOCK * 3, y - Sizes.BLOCK * 5, Sizes.BLOCK * 6, Sizes.BLOCK), WallBlockType.DIRT);
  }

  void generateLadders() {
    for (int i = 0; i < 10; i++) {
      int y = startingY - 2 * Sizes.BLOCK * (i + 1);
      backgrounds.add(new LadderBackground(STARTING_X + Sizes.BLOCK * 4, y));
    }
    for (int i = 0; i < 10; i++) {
      int y = startingY - 2 * Sizes.BLOCK * 10;
      backgrounds.add(new LadderBackground(STARTING_X + 2 * i * Sizes.BLOCK, y));
    }
  }

  void addFallingStars() {
    for (int i = 0; i < 5; i++) {
      Rock rock = new Rock(STARTING_X + Sizes.BLOCK * i, startingY - (20 + 5 * i) * Sizes.BLOCK, false);
      addToWorld(rock);
      freeFallController.addCanFall(rock);
    }
  }

  private boolean addToWorld(DroppableObject dropObject) {
    if (!movingObjects.add(dropObject, true)) return false;
    renderQueue.add(dropObject);
    return true;
  }

  void makeCage(int x, int y) {
    solidBlocks.getBackground().setRectUnscaled(new Rectangle(x - Sizes.BLOCK * 3, y - Sizes.BLOCK * 4, Sizes.BLOCK * 6, Sizes.BLOCK * 4), BackgroundBlockType.DIRT);
    // bottom
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x - Sizes.BLOCK * 3, y, Sizes.BLOCK * 6, Sizes.BLOCK), WallBlockType.DIRT));
    // left, smaller
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x - Sizes.BLOCK * 4, y - Sizes.BLOCK * 7, Sizes.BLOCK, Sizes.BLOCK * 4), WallBlockType.DIRT));
    // right
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x + Sizes.BLOCK * 3, y  - Sizes.BLOCK * 7, Sizes.BLOCK, Sizes.BLOCK * 8), WallBlockType.DIRT));
  }

  void makeVault(int x, int y) {
    // Bottom
    solidBlocks.getBlocks().setRectUnscaled(new Rectangle(x - Sizes.BLOCK, y, Sizes.BLOCK * 18, Sizes.BLOCK), WallBlockType.MAGIC_BRICK_WALL);
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x, y - Sizes.BLOCK, Sizes.BLOCK * 16, Sizes.BLOCK), WallBlockType.MAGIC_BRICK_WALL));
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x + Sizes.BLOCK, y - 2 * Sizes.BLOCK, Sizes.BLOCK * 14, Sizes.BLOCK), WallBlockType.MAGIC_BRICK_WALL));
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x + 2 * Sizes.BLOCK, y - 3 * Sizes.BLOCK, Sizes.BLOCK * 12, Sizes.BLOCK), WallBlockType.MAGIC_BRICK_WALL));
    // Top
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x + 2 * Sizes.BLOCK, y - 9 * Sizes.BLOCK, Sizes.BLOCK * 12, Sizes.BLOCK), WallBlockType.MAGIC_BRICK_WALL));
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x + 3 * Sizes.BLOCK, y - 10 * Sizes.BLOCK, Sizes.BLOCK * 10, Sizes.BLOCK), WallBlockType.MAGIC_BRICK_WALL));
    // Inside
    solidBlocks.getBackground().setRectUnscaled(new Rectangle(x + 2 * Sizes.BLOCK, y - 8 * Sizes.BLOCK, Sizes.BLOCK * 12, 5 * Sizes.BLOCK), BackgroundBlockType.VAULT);
    backgrounds.add(new Portal(x + 8 * Sizes.BLOCK - (int) (1.5 * Sizes.BLOCK), y - 8 * Sizes.BLOCK, null));
  }

  private void addZombieCage(int villageY) {
    makeCage(STARTING_X + Sizes.BLOCK * 14, villageY - Sizes.BLOCK * 10);
    Zombie zombie = new Zombie(STARTING_X + Sizes.BLOCK * 10, villageY - Sizes.BLOCK * 14);
    zombie.addTo(movingObjects, renderQueue, updateQueue);
  }

  private boolean addBird(int x, int y) {
    Bird bird = new Bird(x, y);
    return bird.addTo(movingObjects, renderQueue, updateQueue);
  }

  private boolean addRabbit(int x, int y) {
    Rabbit rabbit = new Rabbit(x, y - RabbitDefinition.HEIGHT);
    return rabbit.addTo(movingObjects, renderQueue, updateQueue);
  }

  private boolean addRooster(int x, int y) {
    Rooster rooster = new Rooster(x, y - RoosterDefinition.HEIGHT);
    return rooster.addTo(movingObjects, renderQueue, updateQueue);
  }

  void generateVillage(int villageY, Random random) {
    startingY = villageY;
    makeHouse(STARTING_X, villageY);
    BackgroundElement fireplaceElement = fireplace.getPlaceableObject(STARTING_X - fireplace.getWidth() / 2, villageY - fireplace.getHeight(), workplaceController).getBackgroundElement();
    backgrounds.add(fireplaceElement);
    makeHouse(STARTING_X + Sizes.BLOCK * 10, villageY);
    backgrounds.add(sawmill.getPlaceableObject(STARTING_X + Sizes.BLOCK * 10 - sawmill.getWidth() / 2, villageY - sawmill.getHeight(), workplaceController).getBackgroundElement());
    generateLadders();
    Assert.executeAndAssert(addBird(STARTING_X, villageY - Sizes.BLOCK * 24));
    Assert.executeAndAssert(addRabbit(STARTING_X, villageY - Sizes.BLOCK));
    Assert.executeAndAssert(addRooster(STARTING_X + Sizes.BLOCK * 1, villageY - Sizes.BLOCK));
    addFallingStars();
    addZombieCage(villageY);
    makeVault(STARTING_X - Sizes.BLOCK * 22, villageY);
    backgrounds.add(new Portal(STARTING_X - 4 * Sizes.BLOCK, villageY - 10 * Sizes.BLOCK, new Portal.Destination(new Vector2f(STARTING_X - 4 * Sizes.BLOCK, villageY - 40 * Sizes.BLOCK))));
    graphController.addSource(fireplaceElement);
    graphController.fillGraph();
    Peon.createAndRegister(STARTING_X + Sizes.BLOCK * 5, (int)(startingY - 10 * Peon.HEIGHT), peonController);
  }

  public void saveToStream(ObjectOutputStream stream) throws IOException {
    stream.writeObject(new Integer(startingY));
  }

  public void loadFromStream(ObjectInputStream stream) throws ClassNotFoundException, IOException {
    this.startingY = (Integer) stream.readObject();
  }
}