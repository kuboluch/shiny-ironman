package kniemkiewicz.jqblocks.ingame.resource;

import kniemkiewicz.jqblocks.ingame.content.resource.Stone;
import kniemkiewicz.jqblocks.ingame.content.resource.Wood;
import kniemkiewicz.jqblocks.ingame.controller.event.EventBus;
import kniemkiewicz.jqblocks.ingame.controller.event.resource.ResourceStorageChangeEvent;
import kniemkiewicz.jqblocks.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  EventBus eventBus;

  final Map<ResourceType, Resource> resourceMap = new HashMap<ResourceType, Resource>();

  // TODO only for test purposes
  @PostConstruct
  public void init() {
    fill(new Wood(1000));
    fill(new Stone(500));
  }

  public void fill(Resource source) {
    if (!resourceMap.containsKey(source.getType())) {
      resourceMap.put(source.getType(), source);
      return;
    }
    Resource resource = resourceMap.get(source.getType());
    source.transferTo(resource);
    eventBus.broadcast(new ResourceStorageChangeEvent());
  }

  public void empty(Resource target, int amount) {
    if (!resourceMap.containsKey(target.getType())) {
      return;
    }
    Resource resource = resourceMap.get(target.getType());
    Assert.assertTrue(resource.getAmount() >= amount);
    resource.transferTo(target, amount);
    eventBus.broadcast(new ResourceStorageChangeEvent());
  }

  public void empty(ResourceType resourceType, int amount) {
    if (!resourceMap.containsKey(resourceType)) {
      return;
    }
    Resource resource = resourceMap.get(resourceType);
    Assert.assertTrue(resource.getAmount() >= amount);
    resource.removeAmount(amount);
    eventBus.broadcast(new ResourceStorageChangeEvent());
  }

  public boolean hasEnoughResource(Resource neededResource) {
    if (!resourceMap.containsKey(neededResource.getType())) {
      return false;
    }
    return resourceMap.get(neededResource.getType()).getAmount() >= neededResource.getAmount();
  }

  public Set<ResourceType> getResourceTypes() {
    return resourceMap.keySet();
  }

  public int getResourceAmount(ResourceType type) {
    assert resourceMap.containsKey(type);
    return resourceMap.get(type).getAmount();
  }
}
