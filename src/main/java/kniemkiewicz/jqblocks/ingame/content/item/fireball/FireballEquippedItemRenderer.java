package kniemkiewicz.jqblocks.ingame.content.item.fireball;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EquippedItemRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 16.09.12
 */
@Component
public class FireballEquippedItemRenderer implements EquippedItemRenderer<FireballItem> {

  ParticleSystem particleSystem;

  @Resource
  Image meteorImage;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  PlayerController playerController;

  @PostConstruct
  void init() {
    particleSystem = newFireSystem();
  }

  public ParticleSystem newFireSystem() {
    ParticleSystem system = new ParticleSystem(meteorImage);
    FireEmitter emitter = new FireEmitter(0, 0);
    system.addEmitter(emitter);
    return system;
  }

  public void update(int delta) {
    particleSystem.update(delta / 2);
  }

  @Override
  public void renderEquippedItem(FireballItem item, Graphics g) {
    float sx = 0.1f;
    float sy = 0.1f;
    g.scale(sx,sy);
    boolean rightFaced = playerController.getPlayer().getXYMovement().getXMovement().getDirection();
    float x1 = pointOfView.getWindowWidth() / 2 + Sizes.BLOCK / 6f * (rightFaced ? 1 : -1);
    float y = pointOfView.getWindowHeight() / 2;
    particleSystem.render(x1 / sx, y/ sy);
    float x2 = pointOfView.getWindowWidth() / 2 - Sizes.BLOCK / 3f * (rightFaced ? 1 : -1);
    particleSystem.render(x2 / sx, y/ sy);
    g.scale(1/sx,1/sy);
  }
}
