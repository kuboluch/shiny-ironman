package kniemkiewicz.jqblocks.ingame.controller;

import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 10/27/12
 */
@Component
public class AgeUpdateController implements UpdateQueue.UpdateController<AgeUpdateController.HasAge>{

  public interface HasAge {
    long getAge();
    void setAge(long age);
  }

  @Override
  public void update(HasAge object, int delta) {
    object.setAge(object.getAge() + delta);
  }
}
