package kniemkiewicz.jqblocks.ingame.object.hp;

import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;

/**
 * User: knie
 * Date: 9/3/12
 */
public interface KillablePhysicalObject<T extends KillablePhysicalObject> extends HasHealthPoints<T>, PhysicalObject {
}
