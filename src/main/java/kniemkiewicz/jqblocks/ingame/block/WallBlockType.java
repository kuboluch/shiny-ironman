package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.renderer.DirtBlockTypeRenderer;
import kniemkiewicz.jqblocks.ingame.block.renderer.SpriteSheetBlockTypeRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: krzysiek
 * Date: 06.08.12
 */
public enum WallBlockType implements RenderableBlockType{
  EMPTY{
    @Override
    public BeanName<? extends Renderer> getRenderer() {
      return null;
    }
  },
  DIRT{
    final BeanName<DirtBlockTypeRenderer> BEAN_NAME = new BeanName<DirtBlockTypeRenderer>(DirtBlockTypeRenderer.class);
    @Override
    public BeanName<? extends Renderer> getRenderer() {
      return BEAN_NAME;
    }
  },
  ROCK {
    final BeanName<SpriteSheetBlockTypeRenderer> BEAN_NAME = new BeanName<SpriteSheetBlockTypeRenderer>(SpriteSheetBlockTypeRenderer.class, "rockBlockTypeRenderer");
    @Override
    public BeanName<? extends Renderer> getRenderer() {
      return BEAN_NAME;
    }
  },
  MAGIC_BRICK_WALL {
    final BeanName<SpriteSheetBlockTypeRenderer> BEAN_NAME = new BeanName<SpriteSheetBlockTypeRenderer>(SpriteSheetBlockTypeRenderer.class, "magicBrickBlockTypeRenderer");
    @Override
    public BeanName<? extends Renderer> getRenderer() {
      return BEAN_NAME;
    }
  },
  SPACE {
    final BeanName<SpriteSheetBlockTypeRenderer> BEAN_NAME = new BeanName<SpriteSheetBlockTypeRenderer>(SpriteSheetBlockTypeRenderer.class, "rockBlockTypeRenderer");
    @Override
    public BeanName<? extends Renderer> getRenderer() {
      return BEAN_NAME;
    }
  },
  ;

  public int getEndurance() {
    return Sizes.DEFAULT_BLOCK_ENDURANCE;
  }

  public RenderableObject.Layer getLayer() {
    return RenderableObject.Layer.WALL;
  }
}
