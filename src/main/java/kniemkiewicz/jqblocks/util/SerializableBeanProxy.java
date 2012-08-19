package kniemkiewicz.jqblocks.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 20.08.12
 */
public class SerializableBeanProxy<T> implements Serializable {

  private transient T bean;
  private BeanName<T> beanName;
  private SerializableBeanProxy(BeanName<T> beanName) {
    this.beanName = beanName;
    this.bean = SpringBeanProvider.CURRENT_PROVIDER.getBean(beanName, true);
  }

  private SerializableBeanProxy(T bean) {
    this.bean = bean;
  }

  // Less type declarations with factory methods as they can guess type.
  public static <T> SerializableBeanProxy<T> getInstance(BeanName<T> beanName) {
    return new SerializableBeanProxy<T>(beanName);
  }

  public static <T> SerializableBeanProxy<T> getInstance(T bean) {
    return new SerializableBeanProxy<T>(bean);
  }

  public T get() {
    return bean;
  }

  private void writeObject(ObjectOutputStream outputStream) throws IOException {
    beanName = SpringBeanProvider.CURRENT_PROVIDER.getNameFor(bean);
    assert beanName != null;
    //perform the default serialization for all non-transient, non-static fields
    outputStream.defaultWriteObject();
  }
  private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
    //always perform the default de-serialization first
    inputStream.defaultReadObject();
    bean = SpringBeanProvider.CURRENT_PROVIDER.getBean(beanName, true);
  }
}
