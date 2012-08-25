package kniemkiewicz.jqblocks.ingame.item;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import kniemkiewicz.jqblocks.util.BeanName;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 10.07.12
 */
public interface Item extends Serializable {

  Class<? extends ItemController> getItemController();

  // renderItem is used only if getItemRenderer returns false.
  BeanName<? extends ItemRenderer> getItemRenderer();

  BeanName<? extends Renderable> getEquippedItemRenderer();

  boolean isLarge();

  boolean isEmpty();
}
