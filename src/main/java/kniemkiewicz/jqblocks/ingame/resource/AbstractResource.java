package kniemkiewicz.jqblocks.ingame.resource;

import kniemkiewicz.jqblocks.util.Assert;

public abstract class AbstractResource implements Resource {
  private final static int PILE_SIZE = 1000;

  protected int amount = 0;

  @Override
  public int getAmount() {
    return amount;
  }

  @Override
  public void addAmount(int amount) {
    this.amount += amount;
  }

  @Override
  public void removeAmount(int amount) {
    Assert.assertTrue(this.amount >= amount);
    this.amount -= amount;
  }

  @Override
  public void transferTo(Resource resource) {
    resource.addAmount(amount);
    this.amount -= amount;
  }

  @Override
  public void transferTo(Resource resource, int amount) {
    Assert.assertTrue(this.amount >= amount);
    this.amount -= amount;
    resource.addAmount(amount);
  }

  @Override
  public void deplete() {
    this.amount = 0;
  }

  @Override
  public int getMaxPileSize() {
    return PILE_SIZE;
  }
}
