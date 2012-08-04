package kniemkiewicz.jqblocks.ingame.object.block;

import kniemkiewicz.jqblocks.ingame.object.NeighborAwareObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: kuboluch
 * Date: 21.07.12
 */
public class NeighborAwareBlock implements NeighborAwareObject<AbstractBlock> {

  private static final long serialVersionUID = 1;

  private List<AbstractBlock> leftNeighbors;
  private List<AbstractBlock> topNeighbors;
  private List<AbstractBlock> rightNeighbors;
  private List<AbstractBlock> bottomNeighbors;

  private boolean leftNeighborsSorted = true;
  private boolean topNeighborsSorted = true;
  private boolean rightNeighborsSorted = true;
  private boolean bottomNeighborsSorted = true;

  @Override
  public void addLeftNeighbor(AbstractBlock neighbor) {
    if (leftNeighbors == null) {
      leftNeighbors = new ArrayList<AbstractBlock>();
    }
    leftNeighbors.add(neighbor);
    leftNeighborsSorted = false;
  }

  @Override
  public void addTopNeighbor(AbstractBlock neighbor) {
    if (topNeighbors == null) {
      topNeighbors = new ArrayList<AbstractBlock>();
    }
    topNeighbors.add(neighbor);
    topNeighborsSorted = false;
  }

  @Override
  public void addRightNeighbor(AbstractBlock neighbor) {
    if (rightNeighbors == null) {
      rightNeighbors = new ArrayList<AbstractBlock>();
    }
    rightNeighbors.add(neighbor);
    rightNeighborsSorted = false;
  }

  @Override
  public void addBottomNeighbor(AbstractBlock neighbor) {
    if (bottomNeighbors == null) {
      bottomNeighbors = new ArrayList<AbstractBlock>();
    }
    bottomNeighbors.add(neighbor);
    bottomNeighborsSorted = false;
  }

  @Override
  public void removeLeftNeighbor(AbstractBlock neighbor) {
    leftNeighbors.remove(neighbor);
  }

  @Override
  public void removeTopNeighbor(AbstractBlock neighbor) {
    topNeighbors.remove(neighbor);
  }

  @Override
  public void removeRightNeighbor(AbstractBlock neighbor) {
    rightNeighbors.remove(neighbor);
  }

  @Override
  public void removeBottomNeighbor(AbstractBlock neighbor) {
    bottomNeighbors.remove(neighbor);
  }

  @Override
  public List<AbstractBlock> getLeftNeighbors() {
    if (leftNeighbors == null) {
      return new ArrayList<AbstractBlock>();
    }
    if (!leftNeighborsSorted) {
      Collections.sort(leftNeighbors, new YCoorComparator());
    }
    return new ArrayList<AbstractBlock>(leftNeighbors);
  }

  @Override
  public List<AbstractBlock> getTopNeighbors() {
    if (topNeighbors == null) {
      return new ArrayList<AbstractBlock>();
    }
    if (!topNeighborsSorted) {
      Collections.sort(topNeighbors, new XCoorComparator());
    }
    return new ArrayList<AbstractBlock>(topNeighbors);
  }

  @Override
  public List<AbstractBlock> getRightNeighbors() {
    if (rightNeighbors == null) {
      return new ArrayList<AbstractBlock>();
    }
    if (!rightNeighborsSorted) {
      Collections.sort(rightNeighbors, new YCoorComparator());
    }
    return new ArrayList<AbstractBlock>(rightNeighbors);
  }


  @Override
  public List<AbstractBlock> getBottomNeighbors() {
    if (bottomNeighbors == null) {
      return new ArrayList<AbstractBlock>();
    }
    if (!bottomNeighborsSorted) {
      Collections.sort(bottomNeighbors, new XCoorComparator());
    }
    return new ArrayList<AbstractBlock>(bottomNeighbors);
  }

  private static class XCoorComparator implements Comparator<AbstractBlock> {
    @Override
    public int compare(AbstractBlock o1, AbstractBlock o2) {
      if (o1.x < o2.x) {
        return -1;
      }
      if (o1.x > o2.x) {
        return 1;
      }
      return 0;
    }
  }

  private static class YCoorComparator implements Comparator<AbstractBlock> {
    @Override
    public int compare(AbstractBlock o1, AbstractBlock o2) {
      if (o1.y < o2.y) {
        return -1;
      }
      if (o1.y > o2.y) {
        return 1;
      }
      return 0;
    }
  }

}
