package kniemkiewicz.jqblocks;

import com.google.common.collect.Lists;
import de.matthiasmann.twl.GUI;
import kniemkiewicz.jqblocks.ingame.MainGameState;
import kniemkiewicz.jqblocks.ingame.controller.EndGameController;
import kniemkiewicz.jqblocks.ingame.controller.SaveGameListener;
import kniemkiewicz.jqblocks.ingame.hud.Initializable;
import kniemkiewicz.jqblocks.twl.BasicTWLGameState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class SpringMainGameStateAdaptor extends BasicTWLGameState implements ApplicationContextAware {

  private static final Log logger = LogFactory.getLog(SpringMainGameStateAdaptor.class);

  public static final String gameContextPath = "context/ingame.xml";

  ApplicationContext mainApplicationContext;

  Stack<ApplicationContext> stateApplicationContextStack = new Stack<ApplicationContext>();

  EndGameController endGameController;

  MainGameState gameState;
  GameContainer gameContainer;
  StateBasedGame stateBasedGame;

  @Override
  public int getID() {
    return 0;
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
    MainGameState.Settings gameSettings = MainGameState.Settings.newDefault(null);
    this.gameContainer = gameContainer;
    this.stateBasedGame = stateBasedGame;
    initInternal(gameSettings);
  }

  void buildAndSwitchContext(MainGameState.Settings gameSettings) throws SlickException {
    // This a fix for some stupid idea-spring conflict on Linux
    ApplicationContext stateApplicationContext = null;
    while (stateApplicationContext == null) {
      try {
        stateApplicationContext = getContext(mainApplicationContext, new String[]{gameContextPath}, new Object[]{});
      } catch (BeanDefinitionStoreException e) {
        logger.error("???", e);
      }
    }
    stateApplicationContextStack.add(stateApplicationContext);
    switchContextToNewest(gameSettings);
    gameState.init(gameContainer, stateBasedGame);
  }
  void switchContextToNewest(MainGameState.Settings gameSettings) throws SlickException {
    ApplicationContext stateApplicationContext = stateApplicationContextStack.lastElement();
    gameState = stateApplicationContext.getBean(MainGameState.class);
    gameState.setSettings(gameSettings);
    endGameController = stateApplicationContext.getBean(EndGameController.class);
    createRootPane();
    ((Game) stateBasedGame).changeRootPane(rootPane);
    onGuiInit(rootPane.getGUI());
    System.gc();
  }

  void initInternal(MainGameState.Settings gameSettings) throws SlickException {
    if (gameSettings.isDefaultRandom()) {
      gameSettings = MainGameState.Settings.newDefault(1L);
    }
    stateApplicationContextStack.clear();
    buildAndSwitchContext(gameSettings);
  }

  public static ApplicationContext getContext(ApplicationContext parent, String[] contextPath, Object[] staticBeans) {
    StaticApplicationContext staticContext = new StaticApplicationContext(parent);
    for (Object bean : staticBeans) {
      staticContext.getDefaultListableBeanFactory().registerSingleton(bean.getClass().getName(), bean);
    }
    staticContext.refresh();
    return new ClassPathXmlApplicationContext(contextPath, true, staticContext);
  }

  @Override
  public void onGuiInit(GUI gui) {
    Map<String, Initializable> initMap = stateApplicationContextStack.lastElement().getBeansOfType(Initializable.class);
    for (Initializable initializable : initMap.values()) {
      initializable.init(gui);
    }
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    gameState.render(gameContainer, stateBasedGame, graphics);
  }

  // This variable allows smoother restarts as we make sure that delta does not jump because of long update time.
  boolean justAfterRestart;

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
    if (endGameController.isGameShouldRestart()) {
      logger.info("Restarting");
      try {
        MainGameState.Settings settings = MainGameState.Settings.newDefault(null);
        if (endGameController.isGameShouldBeLoaded()) {
          FileInputStream input = new FileInputStream(SaveGameListener.FILENAME);
          BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
          ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
          settings = MainGameState.Settings.newFromSavegame(objectInputStream);
          justAfterRestart = true;
        }
        initInternal(settings);
        return;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }
    if (endGameController.isGameShouldEnd()) {
      gameContainer.exit();
    }
    if (justAfterRestart) {
      delta = 0;
    }
    gameState.update(gameContainer, stateBasedGame, delta);
    justAfterRestart = false;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.mainApplicationContext = applicationContext;
  }

  @Override
  protected void createRootPane() {
    gameState.createRootPane();
    rootPane = gameState.getRootPane();
  }
}
