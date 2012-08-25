package kniemkiewicz.jqblocks.ingame.content.resource;

import kniemkiewicz.jqblocks.ingame.resource.AbstractResource;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;

public class Wood extends AbstractResource implements Resource {
  private static final long serialVersionUID = 1;

  public Wood() {
  }

  public Wood(int amount) {
    this.amount = amount;
  }

  @Override
  public ResourceType getType() {
    return ResourceType.WOOD;
  }
}
