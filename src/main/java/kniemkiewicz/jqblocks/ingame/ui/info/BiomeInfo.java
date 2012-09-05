package kniemkiewicz.jqblocks.ingame.ui.info;

import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseMovedEvent;
import kniemkiewicz.jqblocks.ingame.level.enemies.RoamingEnemiesController;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BiomeInfo implements Renderable {

  @Autowired
  RoamingEnemiesController roamingEnemiesController;

  @Override
  public void render(Graphics g) {
    g.setColor(Color.white);
    g.drawString(roamingEnemiesController.getCurrentBiome().getClass().getSimpleName().replace("Biome", ""), 4, 53);
  }

  @Override
  public boolean isDisposable() {
    return false;
  }
}
