package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderBackground;
import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: krzysiek
 * Date: 26.09.12
 */
@Component
public class PathGraph {

  static public final EnumSet<CollisionController.ObjectType> PATHS =
      EnumSet.of(CollisionController.ObjectType.PATHS);

  @Autowired
  CollisionController collisionController;

  @Autowired
  RenderQueue renderQueue;

  Set<PhysicalObject> sources = new HashSet<PhysicalObject>();

  Edge addEdge(Edge e) {
    collisionController.add(PATHS, e, false);
    renderQueue.add(e);
    return e;
  }

  void addSource(PhysicalObject object) {
    sources.add(object);
  }
}
