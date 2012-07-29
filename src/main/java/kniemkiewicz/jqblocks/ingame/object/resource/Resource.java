package kniemkiewicz.jqblocks.ingame.object.resource;

import java.io.Serializable;

public interface Resource extends Serializable {

  public int getAmount();

  public void deplete();

  public String getType();

  public int getMaxPileSize();

  void addAmount(int amount);

  void removeAmount(int amount);

  public void transferTo(Resource resource);

  public void transferTo(Resource resource, int amount);

}
