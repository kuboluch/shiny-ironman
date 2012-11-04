package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.hud.info.TimingInfo;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.util.closure.Closure;
import kniemkiewicz.jqblocks.ingame.util.closure.OnceXTimes;
import kniemkiewicz.jqblocks.util.TimeLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * User: krzysiek
 * Date: 22.10.12
 */
@Component
public class GraphController {
  @Autowired
  PathGraph pathGraph;

  @Autowired
  GraphGenerator graphGenerator;

  @Autowired
  TimingInfo timingInfo;

  @Autowired
  Configuration configuration;

  private OnceXTimes<GraphController> fillGraphClosure;

  @PostConstruct
  void init () {
    fillGraphClosure = new OnceXTimes<GraphController>(configuration.getInt("GraphController.GRAPH_CLOSURE_DELAY", 100),
        false, new Closure<GraphController>() {
      @Override
      public void run(GraphController controller) {
        controller.fillGraph();
      }
    });
  }

  List<PhysicalObject> sources = new ArrayList<PhysicalObject>();

  public void addSource(PhysicalObject source) {
    sources.add(source);
  }

  public void fillGraph() {
    TimingInfo.Timer t = timingInfo.getTimer("fillGraph");
    pathGraph.clear();
    for (PhysicalObject source : sources) {
      graphGenerator.addSource(source);
    }
    t.record();
  }

  public void update() {
    fillGraphClosure.maybeRunWith(this);
  }
}
