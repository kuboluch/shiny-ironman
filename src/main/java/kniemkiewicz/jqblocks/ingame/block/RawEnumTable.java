package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.io.Serializable;
import java.util.EnumMap;

/**
 * User: krzysiek
 * Date: 06.08.12
 */
public class RawEnumTable<T extends Enum<T> & RenderableBlockType> implements Serializable, RenderableObject<RawEnumTable<T>> {

  Object[][] data;
  T emptyType;
  transient EnumMap<T, RenderableBlockType.Renderer> rendererCache;

  public RawEnumTable(T emptyType, int width, int height) {
    assert height > 0;
    assert width > 0;
    data = new Object[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        data[i][j] = emptyType;
      }
    }
    this.emptyType = emptyType;
  }

  final T get(int x, int y) {
    return (T) data[x][y];
  }

  final void set(int x, int y, T type) {
    data[x][y] = type;
  }

  @Override
  public BeanName<? extends ObjectRenderer<? super RawEnumTable>> getRenderer() {
    return null;
  }

  public void fillRendererCache(SpringBeanProvider springBeanProvider) {
    // emptyType.getClass() is NOT enum type.
    rendererCache = new EnumMap<T, RenderableBlockType.Renderer>((Class<T>) emptyType.getDeclaringClass());
    for (T key : ((Class<T>)emptyType.getDeclaringClass()).getEnumConstants()) {
      if (key.getRenderer() != null) {
        rendererCache.put(key, springBeanProvider.getBean(key.getRenderer(), true));
      } else {
        rendererCache.put(key, null);
      }
    }
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    // Someone forgot to call fillRendererCache?
    assert rendererCache != null;
    int x0 = (pov.getShiftX() - Sizes.MIN_X) / Sizes.BLOCK;
    int y0 = (pov.getShiftY() - Sizes.MIN_Y) / Sizes.BLOCK;
    int width = pov.getWindowWidth() / Sizes.BLOCK + 1;
    int height = pov.getWindowHeight() / Sizes.BLOCK + 3;
    if (x0 < 0) {
      x0 = 0;
    }
    if (y0 < 0) {
      y0 = 0;
    }
    if (x0 + width > data.length) {
      width = data.length - x0;
    }
    if (y0 + height > data[0].length) {
      height = data[0].length - y0;
    }
    for (int x = x0; x < x0 + width; x++) {
      int currentFirstY = y0;
      T currentType = emptyType;
      int xx = x * Sizes.BLOCK + Sizes.MIN_X;
      for (int y = y0; y < y0 + height; y++) {
        if (currentType != data[x][y]) {
          int yy1 = currentFirstY * Sizes.BLOCK + Sizes.MIN_Y;
          int height2 = (y - currentFirstY) * Sizes.BLOCK;
          RenderableBlockType.Renderer r1 = rendererCache.get(currentType);
          if (r1 != null) {
            r1.renderBlock(xx, yy1, Sizes.BLOCK, height2, g);
            r1.renderBorder(xx, yy1 + height2, Sizes.BLOCK, RenderableBlockType.Border.BOTTOM, (T)data[x][y], g);
          }
          RenderableBlockType.Renderer r2 = rendererCache.get((T)data[x][y]);
          if (r2 != null) {
            r2.renderBorder(xx, yy1 + height2, Sizes.BLOCK, RenderableBlockType.Border.TOP, currentType, g);
          }
          currentType = (T)data[x][y];
          currentFirstY = y;
        }
      }
      int y = y0 + height;
      int yy1 = currentFirstY * Sizes.BLOCK + Sizes.MIN_Y;
      int height2 = (y - currentFirstY - 1) * Sizes.BLOCK;
      RenderableBlockType.Renderer r = rendererCache.get(currentType);
      if (r != null) {
        r.renderBlock(xx, yy1, Sizes.BLOCK, height2, g);
      }
    }
    for (int x = x0 + 1; x < x0 + width; x++) {
      int currentFirstY = y0;
      T currentType = emptyType;
      T currentTypeLeft = emptyType;
      int xx = x * Sizes.BLOCK + Sizes.MIN_X;
      for (int y = y0; y < y0 + height; y++) {
        if ((currentType != data[x][y])||(currentTypeLeft != data[x - 1][y])) {
          int yy1 = currentFirstY * Sizes.BLOCK + Sizes.MIN_Y;
          int height2 = (y - currentFirstY) * Sizes.BLOCK;
          RenderableBlockType.Renderer r1 = rendererCache.get(currentType);
          if (r1 != null) {
            r1.renderBorder(xx, yy1, height2, RenderableBlockType.Border.LEFT, currentTypeLeft, g);
          }
          RenderableBlockType.Renderer r2 = rendererCache.get(currentTypeLeft);
          if (r2 != null) {
            r2.renderBorder(xx, yy1, height2, RenderableBlockType.Border.RIGHT, currentType, g);
          }
          currentType = (T)data[x][y];
          currentFirstY = y;
          currentTypeLeft = (T)data[x - 1][y];
        }
      }
      int y = y0 + height;
      int yy1 = currentFirstY * Sizes.BLOCK + Sizes.MIN_Y;
      int height2 = (y - currentFirstY - 1) * Sizes.BLOCK;
      RenderableBlockType.Renderer r1 = rendererCache.get(currentType);
      if (r1 != null) {
        r1.renderBorder(xx, yy1, height2, RenderableBlockType.Border.LEFT, currentTypeLeft, g);
      }
      RenderableBlockType.Renderer r2 = rendererCache.get(currentTypeLeft);
      if (r2 != null) {
        r2.renderBorder(xx, yy1, height2, RenderableBlockType.Border.RIGHT, currentType, g);
      }
    }
  }


  @Override
  public Layer getLayer() {
    return emptyType.getLayer();
  }

  @Override
  public Shape getShape() {
    return new Rectangle(Sizes.MIN_X, Sizes.MIN_Y, Sizes.MAX_X - Sizes.MIN_X, Sizes.MAX_Y - Sizes.MIN_Y);
  }

  public void setRectUnscaled(Rectangle shape, T type) {
    final int x0 = Math.round((shape.getX() - Sizes.MIN_X) / Sizes.BLOCK);
    final int y0 = Math.round((shape.getY() - Sizes.MIN_Y) / Sizes.BLOCK);
    final int width = Math.round(shape.getWidth() / Sizes.BLOCK);
    final int height = Math.round(shape.getHeight() / Sizes.BLOCK);
    for (int x = x0; x < x0 + width; x++) {
      for (int y = y0; y < y0 + height; y++) {
        data[x][y] = type;
      }
    }
  }

  public T getValueForUnscaledPoint(int x, int y) {
    return (T) data[(x - Sizes.MIN_X) / Sizes.BLOCK][(y - Sizes.MIN_Y) / Sizes.BLOCK];
  }
}
