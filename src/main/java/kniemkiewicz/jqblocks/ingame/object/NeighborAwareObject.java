package kniemkiewicz.jqblocks.ingame.object;

import java.util.List;

/**
 * User: kuboluch
 * Date: 21.07.12
 */
public interface NeighborAwareObject<T> {

  public void addLeftNeighbor(T neighbor);

  public void addTopNeighbor(T neighbor);

  public void addRightNeighbor(T neighbor);

  public void addBottomNeighbor(T neighbor);

  public List<T> getLeftNeighbors();

  public List<T> getTopNeighbors();

  public List<T> getRightNeighbors();

  public List<T> getBottomNeighbors();

}