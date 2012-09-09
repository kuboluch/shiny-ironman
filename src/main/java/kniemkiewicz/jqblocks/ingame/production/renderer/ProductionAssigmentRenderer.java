package kniemkiewicz.jqblocks.ingame.production.renderer;

import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.object.ProgressBar;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignment;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignmentQueue;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * User: qba
 * Date: 02.09.12
 */
public class ProductionAssigmentRenderer implements Renderable {
  public static final int ASSIGNMENT_IMAGE_SIZE = 25;
  public static final int ASSIGNMENT_IMAGE_ROUNDING = 3;

  ProductionAssignmentQueue assignmentQueue;
  int x;
  int y;

  public ProductionAssigmentRenderer(ProductionAssignmentQueue assignmentQueue, int x, int y) {
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
      g.fillRoundRect(x + i, y - i, ASSIGNMENT_IMAGE_SIZE, ASSIGNMENT_IMAGE_SIZE, ASSIGNMENT_IMAGE_ROUNDING);
      g.setColor(Color.lightGray);
      g.drawRoundRect(x + i, y - i, ASSIGNMENT_IMAGE_SIZE, ASSIGNMENT_IMAGE_SIZE, ASSIGNMENT_IMAGE_ROUNDING);
    }
    item.getRenderer().renderItem(item.createItem(), x + ASSIGNMENT_IMAGE_ROUNDING, y + ASSIGNMENT_IMAGE_ROUNDING, ASSIGNMENT_IMAGE_SIZE - 2 * ASSIGNMENT_IMAGE_ROUNDING, false);
    ProgressBar.render(g, x - 3, y + ASSIGNMENT_IMAGE_SIZE - 3, ASSIGNMENT_IMAGE_SIZE + 6, 6, activeAssignment.getProgress());
  }

  @Override
  public boolean isDisposable() {
    return true;
  }
}
