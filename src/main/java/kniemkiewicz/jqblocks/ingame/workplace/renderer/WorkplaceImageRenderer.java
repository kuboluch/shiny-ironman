package kniemkiewicz.jqblocks.ingame.workplace.renderer;


import com.google.common.base.Optional;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignment;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignmentController;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignmentQueue;
import kniemkiewicz.jqblocks.ingame.production.renderer.ProductionAssigmentRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.BeanAwareImageRendererImpl;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.XMLPackedSheet;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: qba
 * Date: 01.09.12
 */
public class WorkplaceImageRenderer extends BeanAwareImageRendererImpl<WorkplaceBackgroundElement> {

  @Autowired
  ProductionAssignmentController productionAssignmentController;

  @Autowired
  RenderQueue renderQueue;

  public WorkplaceImageRenderer(String imagePath) {
    super(imagePath);
  }

  public WorkplaceImageRenderer(Image image) {
    super(image);
  }

  public WorkplaceImageRenderer(XMLPackedSheet sheet, String imageName) {
    super(sheet, imageName);
  }

  @Override
  public void render(WorkplaceBackgroundElement object, Graphics g, PointOfView pov) {
    Optional<ProductionAssignmentQueue> assignmentQueue = productionAssignmentController.getAssigments(object);
    if (assignmentQueue.isPresent() && assignmentQueue.get().getActiveAssigment().isPresent()) {
      int assignmentImageX = (int) (object.getShape().getCenterX() - ProductionAssigmentRenderer.ASSIGNMENT_IMAGE_SIZE / 2) - pov.getShiftX();
      int assignmentImageY = (int) (object.getShape().getY() - ProductionAssigmentRenderer.ASSIGNMENT_IMAGE_SIZE - 12) - pov.getShiftY();
      renderQueue.add(new ProductionAssigmentRenderer(assignmentQueue.get(), assignmentImageX, assignmentImageY));
    }
    super.render(object, g, pov);
  }
}
