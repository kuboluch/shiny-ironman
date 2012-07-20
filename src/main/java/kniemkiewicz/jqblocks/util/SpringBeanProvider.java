package kniemkiewicz.jqblocks.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

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

  public <T> T getBean(Class<T> clazz) {
    return beanFactory.getBean(clazz);
  }
}
