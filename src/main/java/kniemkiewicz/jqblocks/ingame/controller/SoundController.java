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

  final public void playUnique(Sound sound) {
    if (!sound.playing()) {
      sound.play(1, soundsVolume);
    }
  }

  final public void play(Sound sound) {
    play(sound, 1);
  }

  final public void play(Sound sound, float volume) {
    sound.play(1, soundsVolume * volume);
  }
}
