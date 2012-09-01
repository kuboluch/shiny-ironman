package kniemkiewicz.jqblocks.ingame.production;

import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;

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
