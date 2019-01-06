package org.sss.simple.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wcherry
 * Date: 7/1/13
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Container {
    private Map<String, BeanHolder> beans = new HashMap<>();

    public Container() {
    }

    public abstract void init();

    public void addBean(String name, Class bean){
        addBean(name, bean, false);
    }


    public void addBean(String name, Class bean, boolean instValue){
        BeanHolder bh = new BeanHolder();
        //bh.name = name;
        bh.bean = bean;
        bh.instValue = instValue;
        beans.put(name, bh);
    }


    public Class getBeanTypeByName(String name){
        return beans.get(name).bean;
    }

    public Object getBean(String name) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        BeanHolder bh = beans.get(name);
        if(bh==null) return null;
        Object o = bh.singleton;
        if(o==null){
            o = bh.bean.newInstance();
            if(!bh.instValue) bh.singleton = o;
            inject(o);
        }
        return o;
    }

    public void inject(Object obj) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        List<String> pnames = getPropertyNames(obj.getClass());
        for(String name : pnames){
            BeanHolder bh = beans.get(name);
            Object o = bh.singleton;
            if(o==null){
                o = bh.bean.newInstance();
                if(!bh.instValue) bh.singleton = o;
                inject(o);
            }

            Method m = obj.getClass().getMethod("set"+Character.toUpperCase(name.charAt(0))+name.substring(1), bh.bean);
            m.invoke(obj, o);
        }
    }

    public List<String> getPropertyNames(Class c){
        List<String> pnames = new ArrayList<>();

        for (Method method : c.getDeclaredMethods()) {
            // method must start with set, return void, have one parameter
            if(method.getName().startsWith("set")
                    && method.getReturnType() == void.class
                    && method.getParameterTypes().length == 1){
                Class<?> clazz = method.getParameterTypes()[0];
                String name = method.getName();

                try {
                    String getter = "get"+name.substring(3);
                    Method m = c.getMethod(getter);
                    if(m.getReturnType() == clazz){
                        String pname = Character.toLowerCase(name.charAt(3))+name.substring(4);
                        pnames.add(pname);
                    }
                } catch (NoSuchMethodException expected) { /*TODO: We should do this without an exception */ }
            }
        }

        return pnames;
    }

    static class BeanHolder{
        //String name;
        Class bean;
        Object singleton;
        boolean instValue = false;
    }
}
