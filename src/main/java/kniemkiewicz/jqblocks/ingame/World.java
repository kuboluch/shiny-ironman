package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.inventory.item.QuickItemInventory;
import kniemkiewicz.jqblocks.ingame.level.VillageGenerator;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.serialization.DeserializationHelper;
import kniemkiewicz.jqblocks.ingame.object.serialization.SerializationHelper;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventory;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * User: knie
 * Date: 7/25/12
 */
@Component
public final class World {

  Log logger = LogFactory.getLog(World.class);

  public RenderQueue getRenderQueue() {
    return renderQueue;
  }

  public UpdateQueue getUpdateQueue() {
    return updateQueue;
  }

  class GameLoadException extends RuntimeException {
    GameLoadException(Exception e) {
      super("Error while loading level:", e);
    }
  }

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  QuickItemInventory inventory;

  @Autowired
  ResourceInventory resourceInventory;

  @Autowired
  SpringBeanProvider springBeanProvider;

  @Autowired
  PlayerController playerController;

  @Autowired
  FreeFallController freeFallController;

  @Autowired
  CollisionController collisionController;

  @Autowired
  Backgrounds backgrounds;

  @Autowired
  VillageGenerator villageGenerator;

  long timestamp = 0;

  public void advanceTime(long delta) {
    timestamp += delta;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void killMovingObject(PhysicalObject object) {
    if (object instanceof RenderableObject) {
      renderQueue.remove((RenderableObject) object);
    }
    if (object instanceof PhysicalObject) {
      movingObjects.remove((PhysicalObject) object);
    }
    if (object instanceof UpdateQueue.ToBeUpdated) {
      updateQueue.remove((UpdateQueue.ToBeUpdated)object);
    }
    collisionController.remove(MovingObjects.OBJECT_TYPES, object);
  }

  BitSet markIndexes(Map<Object, Integer> indexes, Iterator<?> objects) {
    BitSet bitSet = new BitSet(indexes.size());
    for (Object o : Collections3.getIterable(objects)) {
      // TODO: make this nicer, somehow.
      if (!(o instanceof RawEnumTable)) {
        bitSet.set(indexes.get(o));
      }
    }
    return bitSet;
  }

  public void serializeGameData(ObjectOutputStream stream) throws IOException {
    Map<Object, Integer> indexes = new HashMap<Object, Integer>();
    List<Object> gameObjects = new ArrayList<Object>();
    List<Iterator<?>> iters = new ArrayList<Iterator<?>>();
    iters.add(renderQueue.iterateAllObjects());
    iters.add(movingObjects.iterateAll());
    iters.add(updateQueue.iterateAll());
    iters.add(freeFallController.getSimpleObjects());
    iters.add(backgrounds.iterateAll());
    for (Object ob : Collections3.iterateOverAllIterators(iters.iterator())) {
      // This class is listed in renderQueue but we do not want to serialize it here. It will be saved as part of
      // solidBlocks.
      if (!(ob instanceof RawEnumTable) && !indexes.containsKey(ob)) {
        indexes.put(ob, gameObjects.size());
        gameObjects.add(ob);
      }
    }
    SerializationHelper.startSerialization();
    for (Object ob : gameObjects) {
      SerializationHelper.add(ob);
    }
    SerializationHelper.flushData(stream);
    stream.writeObject(markIndexes(indexes, renderQueue.iterateAllObjects()));
    stream.writeObject(markIndexes(indexes, movingObjects.iterateAll()));
    stream.writeObject(markIndexes(indexes, updateQueue.iterateAll()));
    stream.writeObject(markIndexes(indexes, freeFallController.getSimpleObjects()));
    stream.writeObject(markIndexes(indexes, backgrounds.iterateAll()));
    inventory.serializeItems(stream);
    resourceInventory.serializeItems(stream);
    solidBlocks.serializeData(stream);
    villageGenerator.saveToStream(stream);
    stream.writeObject(new Long(timestamp));
    stream.close();
  }

  public void loadGameData(ObjectInputStream stream) {
    Player player = null;
    try {
      List<?> gameObjects = DeserializationHelper.deserializeObjects(stream);
      logger.debug(gameObjects);
      for (Object ob : gameObjects) {
        if (ob instanceof Player) {
          player = (Player) ob;
          break;
        }
      }
      assert player != null;
      {
        BitSet renderableObjects = (BitSet)stream.readObject();
        for (int i = 0; i < renderableObjects.size(); i++) {
          if (renderableObjects.get(i)) {
            renderQueue.add((RenderableObject) gameObjects.get(i));
          }
        }
      }
      {
        BitSet movingObjectsSet = (BitSet)stream.readObject();
        for (int i = 0; i < movingObjectsSet.size(); i++) {
          if (movingObjectsSet.get(i)) {
            Assert.executeAndAssert(movingObjects.add((PhysicalObject) gameObjects.get(i), false));
          }
        }
      }
      {
        BitSet toBeUpdated = (BitSet)stream.readObject();
        for (int i = 0; i < toBeUpdated.size(); i++) {
          if (toBeUpdated.get(i)) {
            updateQueue.add((UpdateQueue.ToBeUpdated) gameObjects.get(i));
          }
        }
      }
      {
        BitSet falling = (BitSet)stream.readObject();
        for (int i = 0; i < falling.size(); i++) {
          if (falling.get(i)) {
            if (gameObjects.get(i) instanceof FreeFallController.CanFall) {
              freeFallController.addCanFall((FreeFallController.CanFall) gameObjects.get(i));
            } else {
              freeFallController.addComplex((HasFullXYMovement) gameObjects.get(i));
            }
          }
        }
      }
      {
        BitSet background = (BitSet)stream.readObject();
        for (int i = 0; i < background.size(); i++) {
          if (background.get(i)) {
            backgrounds.add((BackgroundElement)gameObjects.get(i));
          }
        }
      }

      inventory.loadSerializedItems(stream);
      resourceInventory.loadSerializedItems(stream);
      solidBlocks.deserializeData(stream);
      villageGenerator.loadFromStream(stream);
      timestamp = (Long)stream.readObject();
    } catch (Exception e) {
      throw new GameLoadException(e);
    }
    postLoad(player);
  }

  private void postLoad(Player player) {
    // Here we can put all small tweaks that are needed to make loaded level work correctly.
    playerController.setPlayer(player);
  }

  public SpringBeanProvider getSpringBeanProvider() {
    return springBeanProvider;
  }

  public CollisionController getCollisionController() {
    return collisionController;
  }
}
