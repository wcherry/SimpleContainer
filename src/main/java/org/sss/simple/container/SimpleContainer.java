package org.sss.simple.container;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wcherry
 * Date: 7/2/13
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleContainer extends Container {
    private File beansDirectory = new File(".", "beans");

    public void setBeansDirectory(File dir){
        beansDirectory = dir;
    }


    public List<String> scanDirectory(File dir, String packageName){
        //System.out.println("Beans Directory: "+dir.getAbsolutePath()+" package: "+packageName);
        List<String> names = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files == null || files.length<1) return names;
        for(File f : files){
            String name = f.getName();
            if(f.isDirectory() && !name.equals(".") && !name.equals("..")){
                List<String> subNames = scanDirectory(f, packageName+f.getName()+".");
                names.addAll(subNames);
            }
            if(name.endsWith(".class")){
                String className = name.substring(0, name.length()-6);
                names.add(packageName+className);
            } else if(f.getName().endsWith(".jar")){
                //TODO: Need to add code to handle jars HERE!
            }

        }
        return names;
    }

    private void initializeClass(String className){
        try {
            int pos = className.lastIndexOf(".");
            String name = className.substring(pos+1);
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
            Class clazz = Class.forName(className);
            Annotation an = clazz.getAnnotation(Bean.class);
            if(an!=null){
                Bean beanAn = (Bean)an;
                name = beanAn.name();
            }
            addBean(name, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public void init() {
        List<String> names = scanDirectory(beansDirectory, "");
        for(String name : names){
            initializeClass(name);
        }
    }
}
