package kniemkiewicz.jqblocks.ingame.production;

import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;

import static com.google.common.base.Preconditions.*;

/**
 * User: qba
 * Date: 01.09.12
 */
public class ProductionAssignment {

  ItemDefinition item;
  float progress = 0;
  boolean completed;

  public ProductionAssignment(ItemDefinition item) {
    this.item = item;
  }

  public static ProductionAssignment on(ItemDefinition item) {
    return new ProductionAssignment(checkNotNull(item));
  }

  public ItemDefinition getItem() {
    return item;
  }

  public void update(int delta) {
    if (completed) return;
    progress += (float) delta / item.getProductionDuration();
    if (progress >= 1.0f) {
      progress = 1.0f;
      completed = true;
    }
  }

  public boolean isCompleted() {
    return completed;
  }

  public float getProgress() {
    return progress;
  }
}
