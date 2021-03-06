package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;

public interface BackgroundElement extends PhysicalObject, RenderableObject {

  public boolean isResource();

  public boolean isWorkplace();

  // If this method returns true, walls below this background cannot be removed.
  public boolean requiresFoundation();
}
