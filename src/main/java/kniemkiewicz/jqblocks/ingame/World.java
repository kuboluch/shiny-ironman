package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.item.Inventory;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.IterableIterator;
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
public class World {

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
  Inventory inventory;

  @Autowired
  SpringBeanProvider springBeanProvider;

  @Autowired
  PlayerController playerController;

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
      bitSet.set(indexes.get(o));
    }
    return bitSet;
  }

  public void serializeGameData(ObjectOutputStream stream) throws IOException {
    Map<Object, Integer> indexes = new HashMap<Object, Integer>();
    List<Object> gameObjects = new ArrayList<Object>();
    List<Iterator<?>> iters = new ArrayList<Iterator<?>>();
    iters.add(renderQueue.iterateAllObjects());
    iters.add(movingObjects.iterateAll());
    iters.add(solidBlocks.iterateAll());
    iters.add(updateQueue.iterateAll());
    for (Object ob : Collections3.iterateOverAllIterators(iters)) {
      indexes.put(ob, gameObjects.size());
      gameObjects.add(ob);
    }
    stream.writeObject(gameObjects);
    stream.writeObject(markIndexes(indexes, renderQueue.iterateAllObjects()));
    stream.writeObject(markIndexes(indexes, movingObjects.iterateAll()));
    stream.writeObject(markIndexes(indexes, solidBlocks.iterateAll()));
    stream.writeObject(markIndexes(indexes, updateQueue.iterateAll()));
    inventory.serializeItems(stream);
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
      BitSet renderableObjects = (BitSet)stream.readObject();
      BitSet movingObjects = (BitSet)stream.readObject();
      BitSet solidBlocks = (BitSet)stream.readObject();
      BitSet toBeUpdated = (BitSet)stream.readObject();
      inventory.loadSerializedItems(stream);
    } catch (Exception e) {
      throw new GameLoadException(e);
    }
    postLoad(player);
  }

  private void postLoad(Player player) {
    // Here we can put all small tweaks that are needed to make loaded level work correctly.
    playerController.setPlayer(player);
  }
}
