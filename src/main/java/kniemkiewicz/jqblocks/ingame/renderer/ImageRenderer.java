package kniemkiewicz.jqblocks.ingame.renderer;

import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Image;

/**
 * User: qba
 * Date: 01.09.12
 */
public interface ImageRenderer<T extends RenderableObject> extends ObjectRenderer<T>, ItemRenderer<Item> {
  Image getImage();
}
