package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.Backgrounds;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.object.background.NaturalDirtBackground;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class DirtBlock extends AbstractBlock {

  public static Color BROWN = new Color(150.0f/255, 75.0f/255, 0);
  public static Color DARK_GREEN = new Color(0, 0.75f, 0);

  public DirtBlock(float x, float y, float width, float height) {
    super(x, y, width, height);
  }

  public DirtBlock(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  @Override
  protected AbstractBlock getSubBlock(AbstractBlock parent, int x, int y, int width, int height) {
    return new DirtBlock(x, y, width, height);
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    renderDirt(g, x, y, height, width);
  }

  @Override
  public Layer getLayer() {
    return Layer.WALL;
  }

  public static void renderDirt(Graphics g, int x, int y, int height, int width) {
    g.setColor(BROWN);
    g.fillRoundRect(x, y, width, height, 5);
    g.setColor(DARK_GREEN);
    g.drawRoundRect(x, y, width, height, 5);
    g.drawRoundRect(x +1, y + 1, width - 2, height - 2, 5);
  }

  public void removeRect(Rectangle rect, SolidBlocks blocks, Backgrounds backgrounds) {
    super.removeRect(rect, blocks, backgrounds);
    backgrounds.add(new NaturalDirtBackground(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
  }
}
