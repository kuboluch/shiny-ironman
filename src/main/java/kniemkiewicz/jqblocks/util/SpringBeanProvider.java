package kniemkiewicz.jqblocks.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
@Component
public final class SpringBeanProvider implements ApplicationContextAware {

  public static SpringBeanProvider CURRENT_PROVIDER = null;

  ApplicationContext applicationContext;

  SpringBeanProvider() {
    CURRENT_PROVIDER = this;
  }

  Map<BeanName, Object>  cache = new WeakHashMap<BeanName, Object>();

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
        return applicationContext.getBean(name.clazz);
      } else {
        return applicationContext.getBean(name.name, name.clazz);
      }
    }
  }

  public <T> BeanName<T> getNameFor(T bean) {
    String[] names = applicationContext.getBeanNamesForType(bean.getClass());
    if (names.length == 0) return null;
    return new BeanName<T>((Class<T>)bean.getClass(), names[0]);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
