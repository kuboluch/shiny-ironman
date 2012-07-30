package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.resource.Resource;

public interface ResourceBackgroundElement<T extends Resource> extends BackgroundElement {

  public Class getMiningItemType();

  public T mine(int delta);

}
