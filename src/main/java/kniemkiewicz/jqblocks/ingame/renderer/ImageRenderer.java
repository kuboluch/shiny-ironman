package kniemkiewicz.jqblocks.ingame.renderer;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.BeanNameAware;

/**
 * User: qba
 * Date: 01.09.12
 */
public interface ImageRenderer<T extends RenderableObject> extends ObjectRenderer<T>, ItemRenderer<Item> {
  Image getImage();
}
