package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.resource.Resource;
import kniemkiewicz.jqblocks.util.Assert;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
public class PlayerResourceController {

  private Map<String, Resource> resources = new LinkedHashMap<String, Resource>();

  public void addResource(Resource newResource) {
    if (!resources.containsKey(newResource.getType())) {
      resources.put(newResource.getType(), newResource);
      return;
    }
    newResource.transferTo(resources.get(newResource.getType()));
  }

  public boolean hasEnoughResource(Resource neededResource) {
    if (!resources.containsKey(neededResource.getType())) {
      return false;
    }
    return resources.get(neededResource.getType()).getAmount() >= neededResource.getAmount();
  }

  public Set<String> getResourceTypes() {
    return resources.keySet();
  }

  public int getResourceAmount(String type) {
    assert resources.containsKey(type);
    return resources.get(type).getAmount();
  }
}
