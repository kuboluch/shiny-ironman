package kniemkiewicz.jqblocks.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
@Component
public final class SpringBeanProvider implements BeanFactoryAware {

  BeanFactory beanFactory;
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  Map<BeanName, Object>  cache = new HashMap<BeanName, Object>();

  public <T> T getBean(Class<T> clazz, boolean doCache) {
    return getBean(new BeanName<T>(clazz), doCache);
  }

  public <T> T getBean(BeanName<T> name, boolean doCache) {
    if (doCache) {
      if (!cache.containsKey(name)) {
        cache.put(name, getBean(name, false));
      }
      return (T) cache.get(name);
    } else {
      if (name.name == null) {
        return beanFactory.getBean(name.clazz);
      } else {
        return beanFactory.getBean(name.name, name.clazz);
      }
    }
  }
}
