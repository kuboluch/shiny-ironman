package kniemkiewicz.jqblocks.ingame.object.resource;

import kniemkiewicz.jqblocks.util.Assert;

public class ResourceBase implements Resource {
  private final static int PILE_SIZE = 10000;

  protected int amount = 0;

  @Override
  public int getAmount() {
    return amount;
  }

  @Override
  public void add(Resource resource) {
    this.amount += resource.getAmount();
  }

  @Override
  public void remove(Resource resource) {
    Assert.assertTrue(this.amount >= resource.getAmount());
    this.amount -= resource.getAmount();
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
  public String getType() {
    return this.getClass().getSimpleName();
  }

  @Override
  public int getMaxPileSize() {
    return PILE_SIZE;
  }
}
