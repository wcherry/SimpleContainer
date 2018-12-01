package org.sss.simple.container;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.sss.xml.XPath;
import org.sss.xml.XmlException;
import org.sss.xml.model.*;
import org.sss.xml.reader.XmlReader;

/**
 * Created by wcherry on 2/18/14.
 */
public class XmlContainer extends Container {
    static final String CP_PREFIX = "classpath:";
    ArrayList<String> configFiles = new ArrayList<String>();
    public void addXmlConfigFile(String name){
        configFiles.add(name);
    }

    void initializeClass(String name, String className){
        try {
            Class clazz = Class.forName(className);
            addBean(name, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public void init() {
        for(String filename : configFiles){
            XmlReader parser = new XmlReader();
            try {
                Reader reader = null;
                if(filename.startsWith(CP_PREFIX)){
                    reader = new InputStreamReader(getClass().getResourceAsStream(filename.substring(CP_PREFIX.length())));
                } else {
                    reader = new FileReader(filename);
                }

//                } else {
//                    reader = new InputStreamReader(new URL(filename).openConnection().getInputStream());
//                }
                Document doc = parser.readerToDocument(reader);
                List<Element> elements = XPath.search(doc.getRoot(), "beans/bean");
                for(Element e : elements){
                    initializeClass(e.getAttribute("name").getValue(), e.getAttribute("class").getValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
    }
}
