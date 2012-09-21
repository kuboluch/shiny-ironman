package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.block.renderer.DirtBlockTypeRenderer;
import kniemkiewicz.jqblocks.ingame.block.renderer.NaturalDirtBackgroundRenderer;
import kniemkiewicz.jqblocks.ingame.block.renderer.SpriteSheetBlockTypeRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: krzysiek
 * Date: 20.09.12
 */
public enum BackgroundBlockType implements RenderableBlockType{
  EMPTY{
    @Override
    public BeanName<? extends RenderableBlockType.Renderer> getRenderer() {
      return null;
    }
  },
  SPACE {
    @Override
    public BeanName<? extends Renderer> getRenderer() {
      return null;
    }
  },
  DIRT {
    final BeanName<NaturalDirtBackgroundRenderer> BEAN_NAME = new BeanName<NaturalDirtBackgroundRenderer>(NaturalDirtBackgroundRenderer.class);
    @Override
    public BeanName<? extends Renderer> getRenderer() {
      return BEAN_NAME;
    }
  },
  VAULT {
    final BeanName<SpriteSheetBlockTypeRenderer> BEAN_NAME = new BeanName<SpriteSheetBlockTypeRenderer>(SpriteSheetBlockTypeRenderer.class, "vaultBackgroundDirtBlockTypeRenderer");
    @Override
    public BeanName<? extends Renderer> getRenderer() {
      return BEAN_NAME;
    }
  },
  ;

  public RenderableObject.Layer getLayer() {
    return RenderableObject.Layer.LOWER_BACKGROUND;
  }
}
