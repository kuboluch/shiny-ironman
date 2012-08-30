package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.FreeFallController;

/**
 * User: qba
 * Date: 22.08.12
 */
public interface DroppableObject<T extends RenderableObject> extends RenderableObject<T>, FreeFallController.CanFall {
}
