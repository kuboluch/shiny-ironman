package kniemkiewicz.jqblocks.ingame;

import de.matthiasmann.twl.GUI;
import kniemkiewicz.jqblocks.ingame.block.MapView;
import kniemkiewicz.jqblocks.ingame.controller.EndGameController;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.controller.SaveGameListener;
import kniemkiewicz.jqblocks.ingame.controller.UIController;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.keyboard.KeyboardInputEventBus;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseInputEventBus;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.inventory.action.PickupItemActionController;
import kniemkiewicz.jqblocks.ingame.level.LevelGenerator;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;

import kniemkiewicz.jqblocks.ingame.level.enemies.RoamingEnemiesController;
import kniemkiewicz.jqblocks.ingame.production.ProductionAssignmentController;
import kniemkiewicz.jqblocks.ingame.production.ProductionController;
import kniemkiewicz.jqblocks.ingame.production.action.PlayerProductionActionController;
import kniemkiewicz.jqblocks.ingame.production.action.WorkplaceProductionActionController;
import kniemkiewicz.jqblocks.ingame.renderer.GraphicsContainer;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventoryController;
import kniemkiewicz.jqblocks.ingame.ui.HealthBar;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import kniemkiewicz.jqblocks.ingame.ui.info.BiomeInfo;
import kniemkiewicz.jqblocks.ingame.ui.info.MouseInputInfo;
import kniemkiewicz.jqblocks.ingame.ui.info.ResourceInfo;
import kniemkiewicz.jqblocks.ingame.ui.info.TimingInfo;
import kniemkiewicz.jqblocks.ingame.ui.inventory.ItemDragController;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceActionController;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.twl.BasicTWLGameState;
import kniemkiewicz.jqblocks.twl.RootPane;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class MainGameState extends BasicTWLGameState {

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
  PickupItemActionController pickupItemActionController;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  EventBus eventBus;

  @Autowired
  KeyboardInputEventBus keyboardInputEventBus;

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
  BiomeInfo biomeInfo;

  @Autowired
  ResourceInfo resourceInfo;

  @Autowired
  InputContainer inputContainer;

  @Autowired
  GraphicsContainer graphicsContainer;

  @Autowired
  WorkplaceController workplaceController;

  @Autowired
  WorkplaceActionController workplaceActionController;

  @Autowired
  ProductionController productionController;

  @Autowired
  ProductionAssignmentController productionAssignmentController;

  @Autowired
  PlayerProductionActionController playerProductionActionController;

  @Autowired
  WorkplaceProductionActionController workplaceProductionActionController;

  @Autowired
  UIController uiController;

  @Autowired
  HealthBar healthBar;

  @Autowired
  MapView mapView;

  @Autowired
  FreeFallController freeFallController;

  @Autowired
  RoamingEnemiesController roamingEnemiesController;

  @Autowired
  ItemDragController itemDragController;

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
    inputListeners.add(mapView);
    eventBus.addListener(uiController);
    eventBus.addListener(workplaceController);
    eventBus.addListener(workplaceActionController);
    eventBus.addListener(itemDragController);
    eventBus.addListener(pickupItemActionController);
    eventBus.addListener(inventoryController);
    eventBus.addListener(resourceInventoryController);
    eventBus.addListener(productionController);
    eventBus.addListener(playerProductionActionController);
    eventBus.addListener(workplaceProductionActionController);
    renderQueue.add(timingInfo);
    renderQueue.add(mouseInputInfo);
    renderQueue.add(resourceInfo);
    renderQueue.add(healthBar);
    renderQueue.add(mapView);
    renderQueue.add(biomeInfo);
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
    graphicsContainer.setGraphics(graphics);
    TimingInfo.Timer t = timingInfo.getTimer(TimingInfo.RENDER_TIMER);
    renderQueue.render(graphics);
    t.record();
  }

  public void singleUpdate(GameContainer gameContainer, int delta) {
    TimingInfo.Timer t = timingInfo.getTimer("update");
    keyboardInputEventBus.update();
    mouseInputEventBus.update();
    eventBus.update();
    workplaceActionController.update(delta);
    roamingEnemiesController.update(delta);
    playerProductionActionController.update(delta);
    workplaceProductionActionController.update(delta);
    for (InputListener l : inputListeners) {
      l.listen(gameContainer.getInput(), delta);
    }
    updateQueue.update(delta);
    freeFallController.update(delta);
    productionAssignmentController.update();
    productionController.update();
    t.record();
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
    world.advanceTime(delta);
    // This happens mostly with breakpoints and generally breaks physics.
    if (delta > 1000) return;
    int maxDelta = 8;
    while (delta > maxDelta) {
      singleUpdate(gameContainer, maxDelta);
      delta -= maxDelta;
    }
    singleUpdate(gameContainer, delta);
  }

  /* Event handling */

  /**
	 * @see org.newdawn.slick.InputListener#keyPressed(int, char)
	 */
  @Override
	public void keyPressed(int key, char c) {
    keyboardInputEventBus.keyPressed(key, c);
	}

	/**
	 * @see org.newdawn.slick.InputListener#keyReleased(int, char)
	 */
  @Override
	public void keyReleased(int key, char c) {
    keyboardInputEventBus.keyReleased(key, c);
	}

  @Override
  public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    mouseInputEventBus.mouseMoved(oldx, oldy, newx, newy);
  }

  @Override
  public void mouseWheelMoved(int newValue) {
    mouseInputEventBus.mouseWheelMoved(newValue);
  }

  @Override
  public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    mouseInputEventBus.mouseDragged(oldx, oldy, newx, newy);
  }

  @Override
  public void mouseClicked(int button, int x, int y, int clickCount) {
    mouseInputEventBus.mouseClicked(button, x, y, clickCount);
  }

  @Override
  public void mousePressed(int button, int x, int y) {
    mouseInputEventBus.mousePressed(button, x, y);
    mouseInputEventBus.blockMousePressedEvent();
  }

  @Override
  public void mouseReleased(int button, int x, int y) {
    mouseInputEventBus.mouseReleased(button, x, y);
    mouseInputEventBus.unblockMousePressedEvent();
  }

  /* UI */

  @Autowired
  MainGameUI ui;

  @Override
  public void onGuiInit(GUI gui) {}

  @Override
  public void createRootPane() {
    super.createRootPane();
    ui.createUI(rootPane);
  }

  public RootPane getRootPane() {
    return rootPane;
  }

  @Override
  protected void layoutRootPane() {
    ui.layoutUI();
  }
}
