/**
 * Created with IntelliJ IDEA.
 * User: wcherry
 * Date: 7/2/13
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */

import beans.*;

import org.junit.Test;
import org.sss.simple.container.Container;
import org.sss.simple.container.SimpleContainer;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class ContainerTest {

    @Test public void testGetPropertyNames() throws Exception {
        Container c = new TestContainer();
        c.init();
        List<String> names = c.getPropertyNames(Foo.class);
        assertSame("Foo class SHOULD have one property",1, names.size());
        assertNotNull("Foo class SHOULD have a property named", names.contains("bar"));
    }

    @Test public void testAddSimpleBean() throws Exception {
        Container c = new TestContainer();
        c.init();
        c.addBean("bar", Bar.class);

        assertNotNull("Container SHOULD have a bar bean", c.getBeanTypeByName("bar"));
    }

    @Test public void testSetterInject() throws Exception {
        Container c = new TestContainer();
        c.init();
        c.addBean("bar", Bar.class);
        Foo f = new Foo();
        c.inject(f);

        assertNotNull("Foo SHOULD have a bar bean set", f.getBar());
    }

    @Test public void testSetterInjectSingletonIsTheSame() throws Exception {
        Container c = new TestContainer();
        c.init();
        c.addBean("bar", Bar.class);
        Foo f = new Foo();
        c.inject(f);
        FooTwo f2 = new FooTwo();
        c.inject(f2);

        assertSame("Foo and Foo2 SHOULD have the same bar bean set", f.getBar(), f2.getBar());
    }

    @Test public void testSetterInjectInstanceIsNotTheSame() throws Exception {
        Container c = new TestContainer();
        c.init();
        c.addBean("bar", Bar.class, true);
        Foo f = new Foo();
        c.inject(f);
        FooTwo f2 = new FooTwo();
        c.inject(f2);

        assertNotSame("Foo and Foo2 SHOULD have a different bar bean set", f.getBar(), f2.getBar());
    }

    @Test public void testSetterInjectWithInjectedBeans() throws Exception {
        Container c = new TestContainer();
        c.init();
        c.addBean("bar", Bar.class);
        c.addBean("barTwo", BarTwo.class);
        FooThree f = new FooThree();
        c.inject(f);

        assertNotNull("FooThree SHOULD have a barTwo bean set", f.getBarTwo());
        assertNotNull("FooThree's barTwo bean SHOULD have a bar bean set", f.getBarTwo().getBar());
    }

    @Test public void testSetterInjectWithInjectedBeansReversed() throws Exception {
        Container c = new TestContainer();
        c.init();
        c.addBean("barTwo", BarTwo.class);
        c.addBean("bar", Bar.class);
        FooThree f = new FooThree();
        c.inject(f);

        assertNotNull("FooThree SHOULD have a barTwo bean set", f.getBarTwo());
        assertNotNull("FooThree's barTwo bean SHOULD have a bar bean set", f.getBarTwo().getBar());
    }
}
