package kniemkiewicz.jqblocks.ingame.content.item.torch;

import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.background.AbstractBackgroundElement;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 29.08.12
 */
public class TorchBackground extends AbstractBackgroundElement<TorchBackground> {

  private static final long serialVersionUID = 1;

  public TorchBackground(int x, int y) {
    super(x, y, TorchDefinition.WIDTH, TorchDefinition.HEIGHT);
  }

  @Override
  public boolean isResource() {
    return true;
  }

  @Override
  public boolean requiresFoundation() {
    return false;
  }

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return TorchDefinition.RENDERER;
  }
}
