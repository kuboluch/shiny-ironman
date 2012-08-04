package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.controller.EndGameController;
import kniemkiewicz.jqblocks.ingame.controller.InventoryController;
import kniemkiewicz.jqblocks.ingame.controller.SaveGameListener;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseInputEventBus;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.level.LevelGenerator;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventoryController;
import kniemkiewicz.jqblocks.ingame.ui.MouseInputInfo;
import kniemkiewicz.jqblocks.ingame.ui.ResourceInfo;
import kniemkiewicz.jqblocks.ingame.ui.TimingInfo;
import kniemkiewicz.jqblocks.twl.TWLStateBasedGame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class Game extends StateBasedGame {

  public Game() {
    super("");
  }

  @Override
  public void initStatesList(GameContainer gameContainer) throws SlickException {
    // State is initialized by SpringGameAdaptor
  }
}