package kniemkiewicz.jqblocks.ingame.resource;

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
