package kniemkiewicz.jqblocks.ingame.content.resource;

import kniemkiewicz.jqblocks.ingame.resource.AbstractResource;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;

/**
 * User: qba
 * Date: 26.08.12
 */
public class Stone extends AbstractResource implements Resource {
  private static final long serialVersionUID = 1;

  public Stone() {
  }

  public Stone(int amount) {
    this.amount = amount;
  }

  @Override
  public ResourceType getType() {
    return ResourceType.STONE;
  }
}
