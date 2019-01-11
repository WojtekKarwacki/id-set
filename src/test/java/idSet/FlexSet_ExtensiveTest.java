package idSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FlexSet_ExtensiveTest {


    private int rep = 256;

    @Test
    public void extensiveTest0() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.add(testObject0(i)));
        }
        assertTrue(flexSet.size() == rep);
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.contains(testObject0(i)));
        }
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.remove(testObject0(i)));
        }
        assertTrue(flexSet.isEmpty());
        for (int i = 0; i < rep; i++) {
            assertTrue(!flexSet.contains(testObject0(i)));
        }
    }

    private TestObject_0 testObject0(int i) {
        return new TestObject_0(i * 31);
    }

    @Test
    public void extensiveTest1() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i = 0; i < rep; i++) {
            flexSet.add(testObject1(i));
        }
        assertTrue(flexSet.size() <= rep);
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.contains(testObject1(i)));
        }
        for (int i = 0; i < rep; i++) {
            flexSet.remove(testObject1(i));
        }
        assertTrue(flexSet.isEmpty());
        for (int i = 0; i < rep; i++) {
            assertTrue(!flexSet.contains(testObject1(i)));
        }
    }

    private TestObject_0 testObject1(int i) {
        return new TestObject_0(((i % 2) * 2 - 1) * i / 2);
    }

    @Test
    public void extensiveTest2() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.add(testObject2(i)));
        }
        assertTrue(flexSet.size() == rep);
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.contains(testObject2(i)));
        }
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.remove(testObject2(i)));
        }
        assertTrue(flexSet.isEmpty());
        for (int i = 0; i < rep; i++) {
            assertTrue(!flexSet.contains(testObject2(i)));
        }
    }

    private TestObject_0 testObject2(int i) {
        return new TestObject_0(i * 512);
    }

    @Test
    public void extensiveTest3() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i = 0; i < rep; i++) {
            flexSet.add(testObject3(i));
        }
        assertTrue(flexSet.size() == rep / 2);
        for (int i = 0; i < rep; i++) {
            assertTrue(flexSet.contains(testObject3(i)));
        }
        for (int i = 0; i < rep; i++) {
            flexSet.remove(testObject3(i));
        }
        assertTrue(flexSet.isEmpty());
        for (int i = 0; i < rep; i++) {
            assertTrue(!flexSet.contains(testObject3(i)));
        }
    }

    private TestObject_0 testObject3(int i) {
        return new TestObject_0(-i / 2);
    }

    @Test
    public void extensiveTest4() {
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        for (int i = 0; i < 32; i++) {
            FlexSet<TestObject_0> flexSet = FlexSet.instance();
            int size = generator.nextInt(2048);
            for (int j = 0; j < size; j++) {
                assertTrue(flexSet.add(testObject4(j)));
            }
            assertTrue(flexSet.size() <= size);
            for (int j = 0; j < size; j++) {
                assertTrue(flexSet.contains(testObject4(j)));
            }
            for (int j = 0; j < size; j++) {
                assertTrue(flexSet.remove(testObject4(j)));
            }
            assertTrue(flexSet.isEmpty());
            for (int j = 0; j < size; j++) {
                assertTrue(!flexSet.contains(testObject4(j)));
            }
        }
    }

    private TestObject_0 testObject4(int i) {
        return new TestObject_0(i);
    }

    @Test
    public void extensiveTest5() {
        for (int j=0; j<1024; j++) {
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

    @Test
    public void extensiveTest6() {
        FlexSet<TestObject_0> flexSet0 = FlexSet.instance();
        FlexSet<TestObject_0> flexSet1 = FlexSet.instance();
        int capacity = flexSet0.capacity;
        int limit = capacity / 8;
        for (int i = 0; i < limit; i++) {
            flexSet0.add(testObject6(i, limit));
            flexSet1.add(testObject6(limit - i - 1, limit));
        }
        assertTrue(flexSet0.containsAll(flexSet1));
        assertTrue(flexSet1.containsAll(flexSet0));
    }

    private TestObject_0 testObject6(int i, int limit) {
        return new TestObject_0(i * limit);
    }

    @Test
    public void extensiveTest7() {
        FlexSet<ComplexTestObject> flexSet = FlexSet.instance();
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.removeId(new ComplexTestObject.IntegerId(i)) == null);
        }
        for (int i=0; i< 128; i++) {
            assertTrue(!flexSet.remove(new ComplexTestObject(new ComplexTestObject.IntegerId(i))));
        }
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.add(new ComplexTestObject(new ComplexTestObject.IntegerId(i))));
        }
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.containsId(new ComplexTestObject.IntegerId(i)));
        }
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.contains(new ComplexTestObject(new ComplexTestObject.IntegerId(i))));
        }
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.removeId(new ComplexTestObject.IntegerId(i)) != null);
        }
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.removeId(new ComplexTestObject.IntegerId(i)) == null);
        }
        for (int i=0; i< 128; i++) {
            assertTrue(!flexSet.remove(new ComplexTestObject(new ComplexTestObject.IntegerId(i))));
        }
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.add(new ComplexTestObject(new ComplexTestObject.IntegerId(i))));
        }
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.containsId(new ComplexTestObject.IntegerId(i)));
        }
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.contains(new ComplexTestObject(new ComplexTestObject.IntegerId(i))));
        }
        for (int i=0; i< 128; i++) {
            assertTrue(flexSet.remove(new ComplexTestObject(new ComplexTestObject.IntegerId(i))));
        }
    }

}
