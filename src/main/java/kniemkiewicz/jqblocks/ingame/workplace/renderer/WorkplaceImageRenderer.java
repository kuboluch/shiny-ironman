package kniemkiewicz.jqblocks.ingame.workplace.renderer;


import com.google.common.base.Optional;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignment;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignmentController;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignmentQueue;
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

  private static final int ASSIGNMENT_IMAGE_SIZE = 25;
  private static final int ASSIGNMENT_IMAGE_ROUNDING = 3;

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
      int assignmentImageX = (int) (object.getShape().getCenterX() - ASSIGNMENT_IMAGE_SIZE / 2) - pov.getShiftX();
      int assignmentImageY = (int) (object.getShape().getY() - ASSIGNMENT_IMAGE_SIZE - 12) - pov.getShiftY();
      renderQueue.add(new ProductionAssigmentRenderer(assignmentQueue.get(), assignmentImageX, assignmentImageY));
    }
    super.render(object, g, pov);
  }

  private static class ProductionAssigmentRenderer implements Renderable {
    ProductionAssignmentQueue assignmentQueue;
    int x;
    int y;

    private ProductionAssigmentRenderer(ProductionAssignmentQueue assignmentQueue, int x, int y) {
      this.assignmentQueue = assignmentQueue;
      this.x = x;
      this.y = y;
    }

    @Override
    public void render(Graphics g) {
      ProductionAssignment activeAssignment = assignmentQueue.getActiveAssigment().get();
      ItemDefinition item  = activeAssignment.getItem();
      int queueSize = assignmentQueue.getAssignmentQueue().size();
      for (int i = queueSize - 1; i >= 0; i--) {
        g.setColor(Color.gray);
        g.fillRoundRect(x + i, y + i, ASSIGNMENT_IMAGE_SIZE, ASSIGNMENT_IMAGE_SIZE, ASSIGNMENT_IMAGE_ROUNDING);
        g.setColor(Color.lightGray);
        g.drawRoundRect(x + i, y + i, ASSIGNMENT_IMAGE_SIZE, ASSIGNMENT_IMAGE_SIZE, ASSIGNMENT_IMAGE_ROUNDING);
      }
      item.getRenderer().renderItem(item.createItem(), g, x + ASSIGNMENT_IMAGE_ROUNDING, y + ASSIGNMENT_IMAGE_ROUNDING, ASSIGNMENT_IMAGE_SIZE - 2 * ASSIGNMENT_IMAGE_ROUNDING, false);
      g.setColor(Color.black);
      g.drawString((int)(activeAssignment.getProgress() * 100) + "%", x - 5, y - 4);
    }

    @Override
    public boolean isDisposable() {
      return true;
    }
  }
}
