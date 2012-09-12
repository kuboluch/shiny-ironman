package kniemkiewicz.jqblocks.ingame.content.player.renderer;

import com.google.common.base.Optional;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.object.TwoFacedImageRenderer;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignmentController;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignmentQueue;
import kniemkiewicz.jqblocks.ingame.production.renderer.ProductionAssigmentRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: qba
 * Date: 02.09.12
 */
public class PlayerRenderer extends TwoFacedImageRenderer {

  @Autowired
  PlayerController playerController;

  @Autowired
  ProductionAssignmentController productionAssignmentController;

  @Autowired
  RenderQueue renderQueue;

  public PlayerRenderer(Image leftImage, int imageWidth) {
    super(leftImage, imageWidth);
  }

  public PlayerRenderer(Image leftImage) {
    super(leftImage);
  }

  @Override
  public void render(Renderable p, Graphics g, PointOfView pov) {
    super.render(p, g, pov);
    renderProductionAssigments(pov);
  }

  private void renderProductionAssigments(PointOfView pov) {
    Optional<ProductionAssignmentQueue> assignmentQueue = productionAssignmentController.getAssigments(playerController.getPlayer());
    if (assignmentQueue.isPresent() && assignmentQueue.get().getActiveAssigment().isPresent()) {
      int assignmentImageX = (int) (playerController.getPlayer().getShape().getCenterX() - ProductionAssigmentRenderer.ASSIGNMENT_IMAGE_SIZE / 2) - pov.getShiftX();
      int assignmentImageY = (int) (playerController.getPlayer().getShape().getY() - ProductionAssigmentRenderer.ASSIGNMENT_IMAGE_SIZE - 12) - pov.getShiftY();
      renderQueue.add(new ProductionAssigmentRenderer(assignmentQueue.get(), assignmentImageX, assignmentImageY));
    }
  }
}