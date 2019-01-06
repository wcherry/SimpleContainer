/**
 * Created with IntelliJ IDEA.
 * User: wcherry
 * Date: 7/2/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
import beans.AnnotatedBar;
import beans.Foo;
import org.junit.Test;
import org.sss.simple.container.SimpleContainer;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleContainerTest {

    static final String BASE_DIRECTORY="./build/classes/java/test";

    @Test public void testGetBeansInDirectory() throws Exception {
        SimpleContainer c = new SimpleContainer();
        List<String> names = c.scanDirectory(new File(BASE_DIRECTORY+"/beans"), "beans.");
        assertSame("Should have 6 beans", 6, names.size());
        assertNotNull("Should have Bar class", names.contains("Bar"));
    }

    @Test public void testInitBeansInDirectory() throws Exception {
        SimpleContainer c = new SimpleContainer();
        c.setBeansDirectory(new File(BASE_DIRECTORY));
        c.init();

        Object obj = c.getBean("foo");
        //c.dumpBeans();
        assertNotNull("Container should have a Foo bean", obj);
        assertEquals("Foo bean should be of type Foo", Foo.class, obj.getClass());
        Foo foo = (Foo)obj;
        assertNotNull("Foo bean should have a bar bean", foo.getBar());
    }


    @Test public void testNonExistentDirectory() throws Exception {
        SimpleContainer c = new SimpleContainer();
        c.setBeansDirectory(new File("./this_directory_does_not_exists"));
        c.init();
    }

    @Test public void testEmptyDirectory() throws Exception {
        SimpleContainer c = new SimpleContainer();
        c.setBeansDirectory(new File(BASE_DIRECTORY+"/test_artifacts/empty"));
        c.init();
    }

    @Test public void testDirectoryWithNoBeans() throws Exception {
        SimpleContainer c = new SimpleContainer();
        c.setBeansDirectory(new File(BASE_DIRECTORY+"/test_artifacts/no_beans"));
        c.init();
    }

    @Test public void testAnnotatedBeansInDirectory() throws Exception {
        SimpleContainer c = new SimpleContainer();
        c.setBeansDirectory(new File(BASE_DIRECTORY));
        c.init();

        Object obj = c.getBean("boo");
        //c.dumpBeans();
        assertNotNull("Container should have a boo bean", obj);
        assertEquals("Boo bean should be of type AnnotatedBar", AnnotatedBar.class, obj.getClass());
    }


}
