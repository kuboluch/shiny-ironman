package kniemkiewicz.jqblocks.ingame.object.resource;

public interface Resource {

  public int getAmount();

  public void add(Resource resource);

  public void remove(Resource resource);

  public void deplete();

  public String getType();

  public int getMaxPileSize();

  void addAmount(int amount);

  void removeAmount(int amount);

  public void transferTo(Resource resource, int amount);

}
