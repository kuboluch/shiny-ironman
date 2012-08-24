package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.item.ItemInventory;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventory;
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
  ItemInventory inventory;

  @Autowired
  ResourceInventory resourceInventory;

  @Autowired
  SpringBeanProvider springBeanProvider;

  @Autowired
  PlayerController playerController;

  @Autowired
  FreeFallController freeFallController;

  long timestamp = 0;

  public void advanceTime(long delta) {
    timestamp += delta;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void killMovingObject(Object object) {
    if (object instanceof RenderableObject) {
      renderQueue.remove((RenderableObject) object);
    }
    if (object instanceof PhysicalObject) {
      movingObjects.remove((PhysicalObject) object);
    }
    if (object instanceof UpdateQueue.ToBeUpdated) {
      updateQueue.remove((UpdateQueue.ToBeUpdated)object);
    }
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
    iters.add(freeFallController.getObjects());
    for (Object ob : Collections3.iterateOverAllIterators(iters.iterator())) {
      // This class is listed in renderQueue but we do not want to serialize it here. It will be saved as part of
      // solidBlocks.
      if (!(ob instanceof RawEnumTable)) {
        indexes.put(ob, gameObjects.size());
        gameObjects.add(ob);
      }
    }
    stream.writeObject(gameObjects);
    stream.writeObject(markIndexes(indexes, renderQueue.iterateAllObjects()));
    stream.writeObject(markIndexes(indexes, movingObjects.iterateAll()));
    stream.writeObject(markIndexes(indexes, updateQueue.iterateAll()));
    stream.writeObject(markIndexes(indexes, freeFallController.getObjects()));
    inventory.serializeItems(stream);
    resourceInventory.serializeItems(stream);
    solidBlocks.serializeData(stream);
    stream.writeObject(new Long(timestamp));
    stream.close();
  }

  public void loadGameData(ObjectInputStream stream) {
    Player player = null;
    try {
      List<Object> gameObjects = (List<Object>)stream.readObject();
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
            movingObjects.add((PhysicalObject) gameObjects.get(i));
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
            freeFallController.add((FreeFallController.CanFall) gameObjects.get(i));
          }
        }
      }

      inventory.loadSerializedItems(stream);
      resourceInventory.loadSerializedItems(stream);
      solidBlocks.deserializeData(stream);
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
}
