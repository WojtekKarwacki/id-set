package idSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FlexSet_SimpleTest {

    @Test
    public void sizeEqual0() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        assertTrue(flexSet.size() == 0);
    }

    @Test
    public void shouldAddElements() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        boolean result = flexSet.add(new TestObject_0(0));
        assertTrue(result);
        assertTrue(flexSet.size() == 1);
    }

    @Test
    public void shouldAssertIfEmpty() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(new TestObject_0(0));
        assertTrue(!flexSet.isEmpty());
    }

    @Test
    public void shouldReturnElementByElement() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        TestObject_0 added = new TestObject_0(0);
        flexSet.add(added);
        TestObject_0 got = flexSet.getByElem(new TestObject_0(0));
        assertTrue(got.equals(added));
    }

    @Test
    public void shouldReturnElement() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        int id = 0;
        TestObject_0 added = new TestObject_0(id);
        flexSet.add(added);
        TestObject_0 got = flexSet.get(id);
        assertTrue(got.equals(added));
    }

    @Test
    public void shouldAssertIfContains() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        TestObject_0 added = new TestObject_0(0);
        flexSet.add(added);
        assertTrue(flexSet.contains(added));
    }

    @Test
    public void shouldAssertIfContainsId() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        int id = 0;
        flexSet.add(new TestObject_0(id));
        assertTrue(flexSet.containsId(id));
    }

    @Test
    public void shouldNotAddElementWhenAlreadyExists() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(new TestObject_0(0));
        boolean result = flexSet.add(new TestObject_0(0));
        assertTrue(!result);
        assertTrue(flexSet.size() == 1);
    }

    @Test
    public void shouldTransformToObjectArray() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        flexSet.add(object0);
        flexSet.add(object1);
        flexSet.add(object2);
        Object[] array = flexSet.toArray();
        assertTrue(array.length == 3);
        for (Object object : array) {
            assertTrue(Arrays.asList(object0, object1, object2).contains(object));
        }
    }

    @Test
    public void shouldTransformToGenericArray() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        flexSet.add(object0);
        flexSet.add(object1);
        flexSet.add(object2);
        Object[] array = flexSet.toArray(new TestObject_0[3]);
        assertTrue(array.length == 3);
        for (Object object : array) {
            assertTrue(Arrays.asList(object0, object1, object2).contains(object));
        }
    }

    @Test
    public void shouldTransformToHashMap() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        Map<Integer, TestObject_0> expectedMap = new HashMap<>();
        for (int i=0; i<5; i++) {
            flexSet.add(new TestObject_0(i));
            expectedMap.put(i, new TestObject_0(i));
        }
        Map<Integer, TestObject_0> actualMap = flexSet.toHashMap();
        assertTrue(expectedMap.equals(actualMap));
    }

    @Test
    public void shouldRemoveElement() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        TestObject_0 object0 = new TestObject_0(0);
        flexSet.add(object0);
        assertTrue(flexSet.remove(object0));
        assertTrue(flexSet.isEmpty());
        assertTrue(!flexSet.contains(object0));
    }

    @Test
    public void shouldRemoveElementById() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        int id = 0;
        flexSet.add(new TestObject_0(id));
        assertTrue(flexSet.removeId(id).equals(new TestObject_0(id)));
        assertTrue(flexSet.isEmpty());
        assertTrue(!flexSet.containsId(id));
    }

    @Test
    public void shouldAssertIfContainsAllGivenCollection() {
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        TestObject_0 object3 = new TestObject_0(3);
        TestObject_0 object4 = new TestObject_0(4);
        Collection<TestObject_0> collection = Arrays.asList(object0, object1, object2, object0);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(object0);
        flexSet.add(object1);
        flexSet.add(object2);
        flexSet.add(object3);
        assertTrue(flexSet.containsAll(collection));
        collection = Arrays.asList(object4);
        assertTrue(!flexSet.containsAll(collection));
    }

    @Test
    public void shouldAssertIfContainsAllIdsGivenCollection() {
        int id0 = 0;
        TestObject_0 object0 = new TestObject_0(id0);
        int id1 = 1;
        TestObject_0 object1 = new TestObject_0(id1);
        int id2 = 2;
        TestObject_0 object2 = new TestObject_0(id2);
        int id3 = 3;
        TestObject_0 object3 = new TestObject_0(id3);
        int id4 = 4;
        Collection<Object> collection = Arrays.asList(id0, id1, id2, id0);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(object0);
        flexSet.add(object1);
        flexSet.add(object2);
        flexSet.add(object3);
        assertTrue(flexSet.containsAllIds(collection));
        collection = Arrays.asList(id4);
        assertTrue(!flexSet.containsAllIds(collection));
    }

    @Test
    public void shouldAddAllGivenCollection() {
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        TestObject_0 object3 = new TestObject_0(3);
        TestObject_0 object4 = new TestObject_0(4);
        Collection<TestObject_0> collection = Arrays.asList(object0, object1, object2, object0, object3, object4);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        boolean result = flexSet.addAll(collection);
        assertTrue(result);
        assertTrue(flexSet.size() == 5);
        assertTrue(flexSet.containsAll(collection));
        assertTrue(collection.containsAll(flexSet));
        result = flexSet.addAll(collection);
        assertTrue(!result);
    }

    @Test
    public void shouldRemoveAllGivenCollection() {
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        TestObject_0 object3 = new TestObject_0(3);
        TestObject_0 object4 = new TestObject_0(4);
        Collection<TestObject_0> collection = Arrays.asList(object1, object2, object3, object4);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(object0);
        flexSet.add(object1);
        boolean result = flexSet.removeAll(collection);
        assertTrue(result);
        assertTrue(flexSet.size() == 1);
        assertTrue(flexSet.contains(object0));
        assertTrue(!flexSet.contains(object1));
        collection = Arrays.asList(object1);
        result = flexSet.removeAll(collection);
        assertTrue(!result);
    }

    @Test
    public void shouldRemoveIdsGivenCollection() {
        int id0 = 0;
        TestObject_0 object0 = new TestObject_0(id0);
        int id1 = 1;
        TestObject_0 object1 = new TestObject_0(id1);
        int id2 = 2;
        int id3 = 3;
        int id4 = 4;
        Collection<Object> collection = Arrays.asList(id1, id2, id3, id4);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(object0);
        flexSet.add(object1);
        boolean result = flexSet.removeAllIds(collection);
        assertTrue(result);
        assertTrue(flexSet.size() == 1);
        assertTrue(flexSet.containsId(id0));
        assertTrue(!flexSet.containsId(id1));
        collection = Arrays.asList(id1);
        result = flexSet.removeAllIds(collection);
        assertTrue(!result);
    }

    @Test
    public void shouldRetainAllGivenCollection() {
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        TestObject_0 object3 = new TestObject_0(3);
        TestObject_0 object4 = new TestObject_0(4);
        Collection<TestObject_0> collection = Arrays.asList(object1, object2, object3, object4);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(object0);
        flexSet.add(object1);
        flexSet.add(object2);
        boolean result = flexSet.retainAll(collection);
        assertTrue(result);
        assertTrue(flexSet.size() == 2);
        assertTrue(!flexSet.contains(object0));
        assertTrue(flexSet.contains(object1));
        assertTrue(flexSet.contains(object2));
        collection = Arrays.asList(object1, object2);
        result = flexSet.retainAll(collection);
        assertTrue(!result);
    }

    @Test
    public void shouldRetainAllIdsGivenCollection() {
        int id0 = 0;
        TestObject_0 object0 = new TestObject_0(id0);
        int id1 = 1;
        TestObject_0 object1 = new TestObject_0(id1);
        int id2 = 2;
        TestObject_0 object2 = new TestObject_0(id2);
        int id3 = 3;
        int id4 = 4;
        Collection<Object> collection = Arrays.asList(id1, id2, id3, id4);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(object0);
        flexSet.add(object1);
        flexSet.add(object2);
        boolean result = flexSet.retainAllIds(collection);
        assertTrue(result);
        assertTrue(flexSet.size() == 2);
        assertTrue(!flexSet.containsId(id0));
        assertTrue(flexSet.containsId(id1));
        assertTrue(flexSet.containsId(id2));
        collection = Arrays.asList(id1, id2);
        result = flexSet.retainAllIds(collection);
        assertTrue(!result);
    }

    @Test
    public void shouldBePossiblyCreatedFromArray() {
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        TestObject_0 object3 = new TestObject_0(3);
        TestObject_0 object4 = new TestObject_0(4);
        FlexSet<TestObject_0> flexSet = FlexSet.fromArray(object0, object1, object2, object3, object4);
        TestObject_0[] array = new TestObject_0[]{object0, object1, object2, object3, object4};
        assertTrue(flexSet.containsAll(Arrays.asList(array)));
        assertTrue(Arrays.equals(array, flexSet.toArray(new TestObject_0[5])));
    }

    @Test
    public void shouldBePossiblyCreatedFromMap() {
        Map<Integer, String> map = new HashMap<>();
        for (int i=0; i<5; i++) {
            map.put(i, String.valueOf(i));
        }
        FlexSet<IdWrapper<Integer, String>> flexSet = FlexSet.fromMap(map);
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            assertTrue(flexSet.get(entry.getKey()).getValue().equals(entry.getValue()));
        }
        for (IdWrapper<Integer, String> idWrapper : flexSet) {
            assertTrue(map.get(idWrapper.getId()).equals(idWrapper.getValue()));
        }
    }

    @Test
    public void shouldOverrideEqualsMethod() {
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        TestObject_0 object3 = new TestObject_0(3);
        TestObject_0 object4 = new TestObject_0(4);
        FlexSet<TestObject_0> flexSet0 = FlexSet.instance();
        FlexSet<TestObject_0> flexSet1 = FlexSet.instance();
        flexSet0.add(object0);
        flexSet0.add(object1);
        flexSet0.add(object2);
        flexSet0.add(object3);
        flexSet0.add(object4);
        flexSet1.add(object0);
        flexSet1.add(object1);
        flexSet1.add(object2);
        flexSet1.add(object3);
        flexSet1.add(object4);
        assertTrue(flexSet0.equals(flexSet1) && flexSet1.equals(flexSet0));
    }

    @Test
    public void shouldOverrideHashCodeMethod() {
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        TestObject_0 object3 = new TestObject_0(3);
        TestObject_0 object4 = new TestObject_0(4);
        FlexSet<TestObject_0> flexSet0 = FlexSet.instance();
        FlexSet<TestObject_0> flexSet1 = FlexSet.instance();
        flexSet0.add(object0);
        flexSet0.add(object1);
        flexSet0.add(object2);
        flexSet0.add(object3);
        flexSet0.add(object4);
        flexSet1.add(object0);
        flexSet1.add(object1);
        flexSet1.add(object2);
        flexSet1.add(object3);
        flexSet1.add(object4);
        assertTrue(flexSet0.hashCode() == flexSet1.hashCode());
    }

    @Test
    public void shouldReturnIdSet() {
        Set<Integer> intSet = new HashSet<>();
        Set<TestObject_0> set = new HashSet<>();
        for (int i=0; i<8; i++) {
            set.add(new TestObject_0(i));
            intSet.add(i);
        }
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.addAll(set);
        Set<Object> idSet = flexSet.idSet();
        assertTrue(idSet.equals(intSet));
    }


    @Test
    public void shouldReturnEntrySet() {
        Set<TestObject_0> set = new HashSet<>();
        for (int i=0; i<5; i++) {
            set.add(new TestObject_0(i));
        }
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.addAll(set);
        Set<TestObject_0> entrySet = flexSet.entrySet();
        assertTrue(entrySet.equals(set));
    }

    @Test
    public void shouldBeIterable() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        int capacity = flexSet.capacity;
        for (int i=0; i<capacity; i++) {
            flexSet.add(new TestObject_0(i));
        }
        for (TestObject_0 testObject : flexSet) {
            assertTrue(testObject != null);
        }
    }

}
