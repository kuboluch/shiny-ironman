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
public class SpringBeanProvider implements BeanFactoryAware {

  BeanFactory beanFactory;
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  Map<Class, Object>  cache = new HashMap<Class, Object>();

  public <T> T getBean(Class<T> clazz, boolean doCache) {
    if (doCache) {
      if (!cache.containsKey(clazz)) {
        cache.put(clazz, getBean(clazz, false));
      }
      return (T) cache.get(clazz);
    } else {
      return beanFactory.getBean(clazz);
    }
  }
}
