package kniemkiewicz.jqblocks;

import kniemkiewicz.jqblocks.ingame.controller.SaveGameListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * User: knie
 * Date: 7/29/12
 */
public class SavegamePrinter {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    FileInputStream fis = new FileInputStream(SaveGameListener.FILENAME);
    ObjectInputStream objectInputStream = new ObjectInputStream(fis);
    while (true) {
      Object o = objectInputStream.readObject();
      System.out.println(o.getClass().getSimpleName() + ":" + o);
    }
  }
}
