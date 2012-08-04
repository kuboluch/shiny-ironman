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
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class MainGameState extends BasicGameState {

  public static class Settings {
    // If not null, level will be generated using it. Otherwise some pseudorandom seed is chosen.
    public Long seed = null;
    // If not null, level will be loaded from this stream. Otherwise it will be generated randomly.
    public ObjectInputStream savegame = null;
  }

  @Autowired
  PlayerController playerController;

  @Autowired
  EndGameController endGameController;

  @Autowired
  SaveGameListener saveGameListener;

  @Autowired
  InventoryController inventoryController;

  @Autowired
  ResourceInventoryController resourceInventoryController;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  EventBus eventBus;

  @Autowired
  MouseInputEventBus mouseInputEventBus;

  @Autowired
  LevelGenerator levelGenerator;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  TimingInfo timingInfo;

  @Autowired
  MouseInputInfo mouseInputInfo;

  @Autowired
  ResourceInfo resourceInfo;

  @Autowired
  InputContainer inputContainer;

  @Autowired
  CollisionController collisionController;

  @Autowired
  World world;

  private Settings settings;

  public void setSettings(Settings settings) {
    // seed and savegame cannot be set at once.
    assert (settings.savegame == null) || (settings.seed == null);
    this.settings = settings;
  }

  List<InputListener> inputListeners = new ArrayList<InputListener>();

  @Override
  public int getID() {
    return 0;
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
    // setSettings should be called first.
    assert settings != null;
    inputContainer.setInput(gameContainer.getInput());
    // TODO rewrite using event bus?
    inputListeners.add(playerController);
    inputListeners.add(endGameController);
    inputListeners.add(saveGameListener);
    inputListeners.add(inventoryController);
    inputListeners.add(resourceInventoryController);
    inputListeners.add(collisionController);
    gameContainer.getInput().addMouseListener(mouseInputEventBus);
    eventBus.addListener(mouseInputInfo);
    eventBus.addListener(inventoryController);
    eventBus.addListener(resourceInventoryController);
    renderQueue.add(timingInfo);
    renderQueue.add(mouseInputInfo);
    renderQueue.add(resourceInfo);
    if (settings.savegame != null) {
      world.loadGameData(settings.savegame);
    } else {
      if (settings.seed != null) {
        levelGenerator.setSeed(settings.seed);
      }
      levelGenerator.generate();
      playerController.initPlayer();
    }
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    TimingInfo.Timer t = timingInfo.getTimer("render");
    renderQueue.render(graphics);
    t.record();
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
    // This happens mostly with breakpoints and generally breaks physics.
    if (delta > 100) return;
    TimingInfo.Timer t = timingInfo.getTimer("update");
    mouseInputEventBus.update();
    eventBus.update();
    for (InputListener l : inputListeners) {
      l.listen(gameContainer.getInput(), delta);
    }
    updateQueue.update(delta);
    t.record();
  }
}
