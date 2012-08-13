package kniemkiewicz.jqblocks.ingame.resource;

/**
 * User: qba
 * Date: 13.08.12
 */
public enum ResourceType {
  WOOD("Wood");

  String name;

  ResourceType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
