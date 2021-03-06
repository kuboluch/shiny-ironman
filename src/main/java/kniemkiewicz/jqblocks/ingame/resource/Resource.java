package kniemkiewicz.jqblocks.ingame.resource;

import java.io.Serializable;

public interface Resource extends Serializable {

  public int getAmount();

  public void deplete();

  public ResourceType getType();

  public int getMaxPileSize();

  void addAmount(int amount);

  void removeAmount(int amount);

  public void transferTo(Resource resource);

  public void transferTo(Resource resource, int amount);

}
