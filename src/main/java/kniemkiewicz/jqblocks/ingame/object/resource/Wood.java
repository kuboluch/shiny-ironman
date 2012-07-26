package kniemkiewicz.jqblocks.ingame.object.resource;

public class Wood implements Resource {
  int amount = 0;

  public Wood() {
  }

  public Wood(int amount) {
    this.amount = amount;
  }

  @Override
  public int getAmount() {
    return amount;
  }

  @Override
  public void add(Resource resource) {
    this.amount += resource.getAmount();
  }
}
