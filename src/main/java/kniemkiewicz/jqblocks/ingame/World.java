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
  }

  public void loadGameData(ObjectInputStream stream) {
    postLoad(null);
  }

  private void postLoad(Player player) {
    player = new Player();
    playerController.setPlayer(player);
  }
}
