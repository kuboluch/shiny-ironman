package kniemkiewicz.jqblocks.util;

import java.io.Serializable;

/**
* User: knie
* Date: 7/29/12
*/
public final class BeanName<T> implements Serializable {


  Class<? extends T> clazz;
  String name;

  public BeanName(Class<? extends T> clazz) {
    assert clazz != null;
    this.clazz = clazz;
  }

  public BeanName(Class<? extends T> clazz, String name) {
    assert clazz != null;
    this.clazz = clazz;
    this.name = name;
  }

  public Class<? extends T> getClazz() {
    return clazz;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BeanName beanName = (BeanName) o;

    if (clazz != null ? !clazz.equals(beanName.clazz) : beanName.clazz != null) return false;
    if (name != null ? !name.equals(beanName.name) : beanName.name != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = clazz != null ? clazz.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }


}
