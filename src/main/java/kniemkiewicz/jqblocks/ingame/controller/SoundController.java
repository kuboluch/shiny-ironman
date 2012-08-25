package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.Configuration;
import org.newdawn.slick.Sound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: knie
 * Date: 8/25/12
 */
@Component
public class SoundController {

  float soundsVolume;

  @Autowired
  Configuration configuration;

  @PostConstruct
  void init() {
    soundsVolume = configuration.getFloat("SOUNDS_VOLUME", 1);
  }

  public void playUnique(Sound sound) {
    if (!sound.playing()) {
      sound.play(1, soundsVolume);
    }
  }

  public void play(Sound sound) {
    sound.play(1, soundsVolume);
  }
}
