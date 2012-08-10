package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.Fireplace;
import kniemkiewicz.jqblocks.ingame.object.background.NaturalDirtBackground;
import kniemkiewicz.jqblocks.ingame.object.block.DirtBlock;
import kniemkiewicz.jqblocks.ingame.workplace.Workplace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: knie
 * Date: 7/30/12
 */
@Component
public class VillageGenerator {

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  Backgrounds backgrounds;

  @Resource(name = "fireplace")
  Workplace fireplace;

  public static final int STARTING_X = (Sizes.MIN_X + Sizes.MAX_X) / 2;

  public static final int VILLAGE_RADIUS = 8;

  int startingY = 0;

  public int getStartingY() {
    assert startingY != 0;
    return startingY;
  }

  void generateVillage(int villageY) {
    startingY = villageY;
    backgrounds.add(new NaturalDirtBackground(STARTING_X - Sizes.BLOCK * 3, villageY  - Sizes.BLOCK * 4, Sizes.BLOCK * 6, Sizes.BLOCK * 4));
    backgrounds.add(fireplace.getPlaceableObject(STARTING_X - Fireplace.WIDTH / 2, villageY - Fireplace.HEIGHT).getBackgroundElement());
    solidBlocks.add(new DirtBlock(STARTING_X - Sizes.BLOCK * 3, villageY  - Sizes.BLOCK * 5, Sizes.BLOCK * 6, Sizes.BLOCK));
  }
}
