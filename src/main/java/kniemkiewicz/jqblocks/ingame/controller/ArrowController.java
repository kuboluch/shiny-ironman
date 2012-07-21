package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.object.Arrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: knie
 * Date: 7/21/12
 */
@Component
public class ArrowController {
  LinkedList<Arrow> arrows = new LinkedList<Arrow>();

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  SolidBlocks blocks;

  @Autowired
  MovingObjects movingObjects;

  public void add(Arrow arrow) {
    arrows.add(arrow);
    renderQueue.add(arrow);
  }

  public void update(int delta) {
    Iterator<Arrow> it = arrows.iterator();
    while (it.hasNext()) {
      Arrow arrow = it.next();
      arrow.update(delta);
      //movingObjects.intersects(arrow.getShape());
    }
  }
}
