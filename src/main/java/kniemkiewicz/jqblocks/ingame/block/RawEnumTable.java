package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

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

  final public T get(int x, int y) {
    return (T) data[x][y];
  }

  final public void set(int x, int y, T type) {
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

  final public int toXIndex(int unscaledX) {
    return (unscaledX - Sizes.MIN_X) / Sizes.BLOCK;
  }

  final public int toYIndex(int unscaledY) {
    return (unscaledY - Sizes.MIN_Y) / Sizes.BLOCK;
  }

  public T getValueForUnscaledPoint(int x, int y) {
    return (T) data[toXIndex(x)][toYIndex(y)];
  }

  // For given shape which does not collide with any blocks, find height at which it would stop by free falling down.
  // Gives bottom border. Takes into account only blocks as obstacles.
  public int getUnscaledDropHeight(Shape unscaledShape) {
    int x1 = toXIndex((int)unscaledShape.getMinX() + 1);
    int x2 = toXIndex((int)unscaledShape.getMaxX()) + 1;
    int y1 = toYIndex((int)unscaledShape.getMaxY() - 1);
    boolean emptyRow = true;
    int y;
    for (y = y1; y < data[0].length;y++) {
      for (int i = x1; i < x2; i++) {
        if (data[i][y] != emptyType) {
          emptyRow = false;
          break;
        }
      }
      if (!emptyRow) break;
    }
    return Sizes.MIN_Y + y * Sizes.BLOCK - 1;
  }

  public boolean collidesWithNonEmpty(Shape shape) {
    Rectangle rect = GeometryUtils.getBoundingRectangle(shape);
    return collidesWithNonEmpty(rect);
  }

  public boolean collidesWithNonEmpty(Rectangle unscaledRect) {
    int x1 = toXIndex(GeometryUtils.toInt(unscaledRect.getX()));
    int x2 = toXIndex(GeometryUtils.toInt(unscaledRect.getMaxX()));
    int y1 = toYIndex(GeometryUtils.toInt(unscaledRect.getY()));
    int y2 = toYIndex(GeometryUtils.toInt(unscaledRect.getMaxY()));
    if ((x1 < 0) || (x2 >= data.length) || (y1 < 0) || (y2 >= data[0].length)) {
      return true;
    }
    for (int i = x1; i <= x2; i++) {
      for (int j = y1; j <= y2; j++) {
        if (data[i][j] != emptyType) return true;
      }
    }
    return false;
  }

  // Rectangles returned by this method are meant to be used with HitResolver, they are not smallest possible ones,
  // some points may be in more than one and so on.
  public List<Rectangle> getIntersectingRectangles(Rectangle unscaledRect) {
    int x1 = toXIndex(GeometryUtils.toInt(unscaledRect.getX()));
    int x2 = toXIndex(GeometryUtils.toInt(unscaledRect.getMaxX()));
    int y1 = toYIndex(GeometryUtils.toInt(unscaledRect.getY()));
    int y2 = toYIndex(GeometryUtils.toInt(unscaledRect.getMaxY()));
    List<Rectangle> rectangles = new ArrayList<Rectangle>();
    if (x1 < 0) {
      rectangles.add(new Rectangle(Sizes.MIN_X - 1000, Sizes.MIN_Y - 1000, 1000, Sizes.MAX_Y - Sizes.MIN_Y + 2000));
      x1 = 0;
    }
    if (x2 >= data.length) {
      rectangles.add(new Rectangle(Sizes.MAX_X, Sizes.MIN_Y - 1000, 1000, Sizes.MAX_Y - Sizes.MIN_Y + 2000));
      x2 = data.length - 1;
    }
    if (y1 < 0) {
      rectangles.add(new Rectangle(Sizes.MIN_X - 1000, Sizes.MIN_Y - 1000, Sizes.MAX_X - Sizes.MIN_X + 2000, 1000));
      y1 = 0;
    }
    if (y2 >= data[0].length) {
      rectangles.add(new Rectangle(Sizes.MIN_X - 1000, Sizes.MAX_Y, Sizes.MAX_X - Sizes.MIN_X + 2000, 1000));
      y2 = data[0].length - 1;
    }
    boolean[][] nonEmpty = new boolean[x2 - x1 + 1][y2 - y1 + 1];
    boolean[][] used = new boolean[x2 - x1 + 1][y2 - y1 + 1];
    for (int x = x1; x <= x2; x++) {
      for (int y = y1; y <= y2; y++) {
        nonEmpty[x - x1][y - y1] = (data[x][y] != emptyType);
      }
    }
    // First lets try to find all long rectangles. This could be even more complicated to cover more corner cases.
    for (int i = 0; i < nonEmpty.length; i++) {
      int firstNonEmpty = -1;
      int j;
      for (j = 0; j < nonEmpty[i].length; j++) {
        if (nonEmpty[i][j]) {
          if (firstNonEmpty < 0) {
            firstNonEmpty = j;
          }
        } else {
          if (firstNonEmpty >= 0) {
            if (j - firstNonEmpty >= 2) {
              for (int z = firstNonEmpty; z < j; z++) {
                used[i][z] = true;
              }
              rectangles.add(new Rectangle(Sizes.MIN_X + (x1 + i) * Sizes.BLOCK, Sizes.MIN_Y + (y1 + firstNonEmpty) * Sizes.BLOCK,
                  Sizes.BLOCK, (j - firstNonEmpty) * Sizes.BLOCK));
            }
            firstNonEmpty = -1;
          }
        }
      }
      if (firstNonEmpty >= 0) {
        if (j - firstNonEmpty >= 2) {
          for (int z = firstNonEmpty; z < j; z++) {
            used[i][z] = true;
          }
          rectangles.add(new Rectangle(Sizes.MIN_X + (x1 + i) * Sizes.BLOCK, Sizes.MIN_Y + (y1 + firstNonEmpty) * Sizes.BLOCK,
              Sizes.BLOCK, (j - firstNonEmpty) * Sizes.BLOCK));
        }
      }
    }
    // Here we do that same bt horizontally. Code is almost the same except for replacing i <-> j in few places.
    for (int i = 0; i < nonEmpty[0].length; i++) {
      int firstNonEmpty = -1;
      int j;
      for (j = 0; j < nonEmpty.length; j++) {
        if (nonEmpty[j][i]) {
          if (firstNonEmpty < 0) {
            firstNonEmpty = j;
          }
        } else {
          if (firstNonEmpty >= 0) {
            if (j - firstNonEmpty >= 2) {
              for (int z = firstNonEmpty; z < j; z++) {
                used[z][i] = true;
              }
              rectangles.add(new Rectangle(Sizes.MIN_X + (x1 + firstNonEmpty) * Sizes.BLOCK, Sizes.MIN_Y + (y1 + i) * Sizes.BLOCK,
                  (j - firstNonEmpty) * Sizes.BLOCK, Sizes.BLOCK));
            }
            firstNonEmpty = -1;
          }
        }
      }
      if (firstNonEmpty >= 0) {
        if (j - firstNonEmpty >= 2) {
          for (int z = firstNonEmpty; z < j; z++) {
            used[z][i] = true;
          }
          rectangles.add(new Rectangle(Sizes.MIN_X + (x1 + firstNonEmpty) * Sizes.BLOCK, Sizes.MIN_Y + (y1 + i) * Sizes.BLOCK,
              (j - firstNonEmpty) * Sizes.BLOCK, Sizes.BLOCK));
        }
      }
    }
    // Now we add remaining small ones.
    for (int x = x1; x <= x2; x++) {
      for (int y = y1; y <= y2; y++) {
        if (nonEmpty[x - x1][y - y1] && ! used[x - x1][y - y1]) {
          rectangles.add(new Rectangle(Sizes.MIN_X + x * Sizes.BLOCK, Sizes.MIN_Y + y * Sizes.BLOCK, Sizes.BLOCK, Sizes.BLOCK));
        }
      }
    }
    return rectangles;
  }

  public int getHeight() {
    return data[0].length;
  }
}
