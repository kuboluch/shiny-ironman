package kniemkiewicz.jqblocks.ingame.production;

import com.google.common.collect.ImmutableList;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;
import kniemkiewicz.jqblocks.ingame.resource.renderer.ResourceRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * User: qba
 * Date: 25.08.12
 */
public class SimpleItemProductionRequirements implements ItemProductionRequirements {

  @Autowired
  ResourceRenderer resourceRenderer;

  @Autowired
  ResourceStorageController resourceStorageController;

  private List<ResourceRequirement> resourceRequirements;

  private List<Item> itemRequirements;

  public SimpleItemProductionRequirements() {}

  @Override
  public List<ResourceRequirement> getResourceRequirements() {
    if (resourceRequirements == null) {
      resourceRequirements = new ArrayList<ResourceRequirement>();
    }
    return resourceRequirements;
  }

  public void setResourceRequirements(List<Resource> resources) {
    this.resourceRequirements = new ArrayList<ResourceRequirement>();
    for (Resource resource : resources) {
      this.resourceRequirements.add(new ResourceRequirement(resource, resourceRenderer, resourceStorageController));
    }
    this.resourceRequirements = ImmutableList.copyOf(groupResourceRequirementsByType());
  }

  private Collection<ResourceRequirement> groupResourceRequirementsByType() {
    Map<ResourceType, ResourceRequirement> resourceRequirementsMap = new HashMap<ResourceType, ResourceRequirement>();
    for (ResourceRequirement resourceRequirement : resourceRequirements) {
      if (!resourceRequirementsMap.containsKey(resourceRequirement.getResource().getType())) {
        resourceRequirementsMap.put(resourceRequirement.getResource().getType(), resourceRequirement);
      } else {
        resourceRequirement.getResource().transferTo(resourceRequirementsMap.get(resourceRequirement.getResource().getType()).getResource());
      }
    }
    return resourceRequirementsMap.values();
  }

  @Override
  public List<Item> getItemRequirements() {
    if (itemRequirements == null) {
      itemRequirements = new ArrayList<Item>();
    }
    return itemRequirements;
  }

  public void setItemRequirements(List<Item> itemRequirements) {
    this.itemRequirements = ImmutableList.copyOf(itemRequirements);
  }
}
