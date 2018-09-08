import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FlexSet_ExtensiveTest {


    private int rep = 131072;

    @Test
    public void extensiveTest0() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.add(new TestObject_0(i * 7)));
        }
        assertTrue(flexSet.size() == rep);
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.contains(new TestObject_0(i * 7)));
        }
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.remove(new TestObject_0(i * 7)));
        }
        assertTrue(flexSet.isEmpty());
        for (int i = 0; i < rep; i++) {
            assertTrue(!flexSet.contains(new TestObject_0(i * 7)));
        }
    }

    @Test
    public void extensiveTest1() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.add(new TestObject_0(i * 7)));
        }
        assertTrue(flexSet.size() == rep);
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.contains(new TestObject_0(i * 7)));
        }
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.remove(new TestObject_0(i * 7)));
        }
        assertTrue(flexSet.isEmpty());
        for (int i = 0; i < rep; i++) {
            assertTrue(!flexSet.contains(new TestObject_0(i * 7)));
        }
    }

    @Test
    public void extensiveTest2() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.add(new TestObject_0(i * 512)));
        }
        assertTrue(flexSet.size() == rep);
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.contains(new TestObject_0(i * 512)));
        }
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.remove(new TestObject_0(i * 512)));
        }
        assertTrue(flexSet.isEmpty());
        for (int i = 0; i < rep; i++) {
            assertTrue(!flexSet.contains(new TestObject_0(i * 512)));
        }
    }

    @Test
    public void extensiveTest3() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i = 0; i < rep; i++) {
            flexSet.add(new TestObject_0(-i / 2));
        }
        assertTrue(flexSet.size() == rep / 2);
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.contains(new TestObject_0(-i / 2)));
        }
        for (int i = 0; i < rep; i++) {
            flexSet.remove(new TestObject_0(-i / 2));
        }
        assertTrue(flexSet.isEmpty());
        for (int i = 0; i < rep; i++) {
            assertTrue(!flexSet.contains(new TestObject_0(-i / 2)));
        }
    }

    @Test
    public void extensiveTest4() {
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        for (int i = 0; i < 32; i++) {
            FlexSet<TestObject_0> flexSet = FlexSet.instance();
            int size = generator.nextInt(2048);
            for (int j = 0; j < size; j++) {
                assertTrue(flexSet.add(new TestObject_0(j)));
            }
            assertTrue(flexSet.size() <= size);
            for (int j = 0; j < size; j++) {
                assertTrue(flexSet.contains(new TestObject_0(j)));
            }
            for (int j = 0; j < size; j++) {
                assertTrue(flexSet.remove(new TestObject_0(j)));
            }
            assertTrue(flexSet.isEmpty());
            for (int j = 0; j < size; j++) {
                assertTrue(!flexSet.contains(new TestObject_0(j)));
            }
        }
    }

    @Test
    public void extensiveTest5() {
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        Set<TestObject_0> set = new HashSet<>();
        for (int i = 0; i < 8192; i++) {
            set.add(new TestObject_0(generator.nextInt(1024)));
        }
        for (TestObject_0 testObject : set) {
            assertTrue(flexSet.add(testObject));
        }
        for (TestObject_0 testObject : set) {
            assertTrue(flexSet.remove(testObject));
        }
        assertTrue(flexSet.isEmpty());
    }

}
