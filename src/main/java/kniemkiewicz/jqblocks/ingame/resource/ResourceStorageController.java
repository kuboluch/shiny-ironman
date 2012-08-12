package kniemkiewicz.jqblocks.ingame.resource;

import kniemkiewicz.jqblocks.util.Assert;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: qba
 * Date: 12.08.12
 */
@Component
public class ResourceStorageController {

  final Map<String, Resource> resourceMap = new HashMap<String, Resource>();

  // TODO only for test purposes
  @PostConstruct
  public void init() {
    fill(new Wood(100));
  }

  public void fill(Resource source) {
    if (!resourceMap.containsKey(source.getType())) {
      resourceMap.put(source.getType(), source);
      return;
    }
    Resource resource = resourceMap.get(source.getType());
    source.transferTo(resource);
  }

  public void empty(Resource target, int amount) {
    if (!resourceMap.containsKey(target.getType())) {
      return;
    }
    Resource resource = resourceMap.get(target.getType());
    Assert.assertTrue(resource.getAmount() >= amount);
    resource.transferTo(target, amount);
  }

  public boolean hasEnoughResource(Resource neededResource) {
    if (!resourceMap.containsKey(neededResource.getType())) {
      return false;
    }
    return resourceMap.get(neededResource.getType()).getAmount() >= neededResource.getAmount();
  }

  public Set<String> getResourceTypes() {
    return resourceMap.keySet();
  }

  public int getResourceAmount(String type) {
    assert resourceMap.containsKey(type);
    return resourceMap.get(type).getAmount();
  }
}
