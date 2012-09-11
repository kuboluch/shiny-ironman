package kniemkiewicz.jqblocks.ingame.content.player.renderer;

import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.QuickItemInventory;
import kniemkiewicz.jqblocks.ingame.item.renderer.DefaultEquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class PlayerEquippmentRenderer implements Renderable {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  QuickItemInventory itemInventory;

  @Autowired
  SpringBeanProvider springBeanProvider;

  @Autowired
  DefaultEquippedItemRenderer defaultEquippedItemRenderer;

  @PostConstruct
  void init() {
    renderQueue.add(this);
  }

  @Override
  public void render(Graphics g) {
    Item item = itemInventory.getSelectedItem();
    if (item == null) return;
    BeanName<? extends EquippedItemRenderer> equippedItemRenderer = item.getEquippedItemRenderer();
    if (equippedItemRenderer != null) {
      springBeanProvider.getBean(equippedItemRenderer, true).renderEquippedItem(item, g);
    } else {
      defaultEquippedItemRenderer.renderEquippedItem(item, g);
    }
  }

  @Override
  public boolean isDisposable() {
    return false;
  }
}
