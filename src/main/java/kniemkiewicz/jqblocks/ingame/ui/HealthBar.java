package kniemkiewicz.jqblocks.ingame.ui;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: krzysiek
 * Date: 10.08.12
 */
@Component
public class HealthBar implements Renderable {

  @Autowired
  PlayerController playerController;

  @Autowired
  PointOfView pov;

  @Override
  public void render(Graphics g) {
    HealthPoints hp = playerController.getPlayer().getHp();
    g.setColor(Color.red);
    g.fillRoundRect(10, pov.getWindowHeight() - 30, 200 * hp.getCurrentHp() / hp.getMaxHp(), 15, 3);
    g.setColor(Color.black);
    g.drawRoundRect(10, pov.getWindowHeight() - 30, 200, 15, 3);
  }
}
