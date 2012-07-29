package kniemkiewicz.jqblocks.ingame.object.resource;

import java.io.Serializable;

public interface Resource extends Serializable {

  public int getAmount();

  public void add(Resource resource);

}
