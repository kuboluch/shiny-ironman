package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.content.block.dirt.NaturalDirtBackground;
import kniemkiewicz.jqblocks.ingame.content.creature.peon.Peon;
import kniemkiewicz.jqblocks.ingame.content.creature.peon.PeonController;
import kniemkiewicz.jqblocks.ingame.content.creature.zombie.Zombie;
import kniemkiewicz.jqblocks.ingame.content.item.rock.Rock;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderBackground;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceDefinition;
import kniemkiewicz.jqblocks.util.Assert;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: knie
 * Date: 7/30/12
 */
@Component
public class VillageGenerator {

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

  public static final int STARTING_X = (Sizes.MIN_X + Sizes.MAX_X) / 2;

  public static final int VILLAGE_RADIUS = 18;

  int startingY = 0;

  public int getStartingY() {
    assert startingY != 0;
    return startingY;
  }

  void makeHouse(int x, int y) {
    backgrounds.add(new NaturalDirtBackground(x - Sizes.BLOCK * 3, y  - Sizes.BLOCK * 4, Sizes.BLOCK * 6, Sizes.BLOCK * 4));
    solidBlocks.add(new Rectangle(x - Sizes.BLOCK * 3, y  - Sizes.BLOCK * 5, Sizes.BLOCK * 6, Sizes.BLOCK), WallBlockType.DIRT);
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
    backgrounds.add(new NaturalDirtBackground(x - Sizes.BLOCK * 3, y - Sizes.BLOCK * 4, Sizes.BLOCK * 6, Sizes.BLOCK * 4));
    // bottom
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x - Sizes.BLOCK * 3, y, Sizes.BLOCK * 6, Sizes.BLOCK), WallBlockType.DIRT));
    // left
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x - Sizes.BLOCK * 4, y - Sizes.BLOCK * 7, Sizes.BLOCK, Sizes.BLOCK * 8), WallBlockType.DIRT));
    // right, smaller
    Assert.executeAndAssert(solidBlocks.add(new Rectangle(x + Sizes.BLOCK * 3, y  - Sizes.BLOCK * 7, Sizes.BLOCK, Sizes.BLOCK * 4), WallBlockType.DIRT));
  }

  private void addZombieCage(int villageY) {
    //makeCage(STARTING_X - Sizes.BLOCK * 10, villageY - Sizes.BLOCK * 10);
    //Zombie zombie = new Zombie(STARTING_X - Sizes.BLOCK * 10, villageY - Sizes.BLOCK * 14);
    //zombie.addTo(movingObjects, renderQueue, updateQueue);
  }

  void generateVillage(int villageY) {
    startingY = villageY;
    makeHouse(STARTING_X, villageY);
    backgrounds.add(fireplace.getPlaceableObject(STARTING_X - fireplace.getWidth() / 2, villageY - fireplace.getHeight(), workplaceController).getBackgroundElement());
    makeHouse(STARTING_X - Sizes.BLOCK * 10, villageY);
    makeHouse(STARTING_X + Sizes.BLOCK * 10, villageY);
    backgrounds.add(sawmill.getPlaceableObject(STARTING_X + Sizes.BLOCK * 10 - sawmill.getWidth() / 2, villageY - sawmill.getHeight(), workplaceController).getBackgroundElement());
    generateLadders();
    Assert.executeAndAssert(Peon.createAndRegister(STARTING_X, (int)(villageY - Peon.HEIGHT), peonController) != null);
    addFallingStars();
    addZombieCage(villageY);
  }

  public void saveToStream(ObjectOutputStream stream) throws IOException {
    stream.writeObject(new Integer(startingY));
  }

  public void loadFromStream(ObjectInputStream stream) throws ClassNotFoundException, IOException {
    this.startingY = (Integer) stream.readObject();
  }
}