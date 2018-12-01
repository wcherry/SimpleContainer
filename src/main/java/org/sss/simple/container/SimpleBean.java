package org.sss.simple.container;

/**
 * Created with IntelliJ IDEA.
 * User: wcherry
 * Date: 7/1/13
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SimpleBean{
    String getBeanName();
    boolean isBeanSingleton();

    void initBean(Container c);
    void addBeanToContainer(Container c);
    void removeBeanFromContainer(Container c);
    void destroyBean(Container c);

//    <Class> getInstance();       // MUST return self if singleton
}
