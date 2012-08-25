package kniemkiewicz.jqblocks.ingame.resource;

/**
 * User: qba
 * Date: 13.08.12
 */
public enum ResourceType {
  WOOD("Wood", "Wood can be aquired from trees"),
  STONE("Stone", "Nothing to say");

  String name;
  String description;

  ResourceType(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
