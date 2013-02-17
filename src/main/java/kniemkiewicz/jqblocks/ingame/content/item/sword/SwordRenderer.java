package kniemkiewicz.jqblocks.ingame.content.item.sword;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.item.bow.BowItem;
import kniemkiewicz.jqblocks.ingame.content.item.bow.BowItemController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.DefaultEquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.GraphicsContainer;
import kniemkiewicz.jqblocks.util.Vector2i;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * User: knie
 * Date: 8/25/12
 */
@Component("equippedSwordRenderer")
public final class SwordRenderer implements EquippedItemRenderer<SwordItem> {

  @Resource
  Image swordImage;

  Image flippedImage;

  @PostConstruct
  void init() {
    flippedImage = swordImage.getFlippedCopy(true, false);
  }

  @Autowired
  DefaultEquippedItemRenderer defaultEquippedItemRenderer;

  @Autowired
  PointOfView pointOfView;

  static final int SIZE = (int) (Sizes.BLOCK * 3.5f);

  public Vector2i getDefaultXY() {
    if (defaultEquippedItemRenderer.isLeftFaced()) {
      return new Vector2i(pointOfView.getWindowWidth() / 2 - SIZE + 6, 2 + pointOfView.getWindowHeight() / 2 - SIZE);
    } else {
      return new Vector2i(pointOfView.getWindowWidth() / 2 - 6, 2 + pointOfView.getWindowHeight() / 2 - SIZE);
    }
  }

  @Override
  public void renderEquippedItem(SwordItem item, Graphics g) {
    Vector2i xy = getDefaultXY();
    if (item.isMoving()) {
      g.pushTransform();
      if (defaultEquippedItemRenderer.isLeftFaced()) {
        g.rotate(xy.getX() + SIZE, xy.getY() + SIZE, - (float)item.getArc());
      } else {
        g.rotate(xy.getX(), xy.getY() + SIZE, (float)item.getArc());
      }
    }
    if (defaultEquippedItemRenderer.isLeftFaced()) {
      flippedImage.draw(xy.getX(), xy.getY(), SIZE, SIZE);
    } else {
      swordImage.draw(xy.getX(), xy.getY(), SIZE, SIZE);
    }
    if (item.isMoving()) {
      g.popTransform();
    }
  }
}
