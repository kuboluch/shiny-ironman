package kniemkiewicz.jqblocks.ingame.inventory.item.controller;

import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;

/**
* User: knie
* Date: 12/30/12
* Time: 1:57 PM
*/
class ActionItemWrapper<E extends Item> implements UpdateQueue.ToBeUpdated<ActionItemWrapper<E>> {

  final E item;
  final Class<? extends UpdateQueue.UpdateController<ActionItemWrapper<E>>> beanName;

  ActionItemWrapper(E item, Class<? extends UpdateQueue.UpdateController<ActionItemWrapper<E>>> beanName) {
    this.item = item;
    this.beanName = beanName;
  }

  public E getItem() {
    return item;
  }

  @Override
  public Class<? extends UpdateQueue.UpdateController<ActionItemWrapper<E>>> getUpdateController() {
    return beanName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ActionItemWrapper that = (ActionItemWrapper) o;

    if (!item.equals(that.item)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return item.hashCode();
  }
}
