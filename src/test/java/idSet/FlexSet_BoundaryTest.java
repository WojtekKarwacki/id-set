package idSet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FlexSet_BoundaryTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowExceptionWhenInitialCapacityIsLessThan1() {
        exception.expect(IllegalArgumentException.class);
        FlexSet.instance(0);
    }

    @Test
    public void shouldThrowExceptionWhenContainsCalledWithNotIdentifiableObject() {
        exception.expect(IllegalArgumentException.class);
        FlexSet.instance().contains(false);
    }

    @Test
    public void shouldServeIdentifierWhichReturnNegativeHashCodes() {
        FlexSet<Identifiable> flexSet = FlexSet.instance();
        TestObject_0 object = new TestObject_0(-1);
        flexSet.add(object);
        assertTrue(flexSet.getByElem(object) != null);

    }

    @Test
    public void shouldThrowExceptionWhenTheArrayPassedToToArrayMethodIsToSmall() {
        exception.expect(IllegalArgumentException.class);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(new TestObject_0(0));
        flexSet.add(new TestObject_0(1));
        flexSet.toArray(new TestObject_0[1]);
    }

    @Test
    public void shouldThrowExceptionWhenTheArrayPassedToToArrayMethodIsNotASubclassOfGenericType() {
        exception.expect(ArrayStoreException.class);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(new TestObject_0(0));
        flexSet.toArray(new TestObject_1[1]);
    }

    @Test
    public void shouldThrowExceptionWhenRemoveCalledWithNotIdentifiableObject() {
        exception.expect(IllegalArgumentException.class);
        FlexSet.instance().remove(false);
    }

    @Test
    public void shouldReturnFalseWhenCallingContainsAllMethodWithIdentifiableObjects() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        flexSet.add(object0);
        flexSet.add(object1);
        flexSet.add(object2);
        boolean result = flexSet.containsAll(Arrays.asList(false));
        assertTrue(!result);
        result = flexSet.containsAll(Arrays.asList(false, object0));
        assertTrue(!result);
    }

    @Test
    public void shouldAddObjectsToTheSameIdRef() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        int capacity = flexSet.capacity;
        int limit = 256;
        for (int i=-limit; i<limit; i++) {
            flexSet.add(new TestObject_0(i*capacity));
        }
        for (int i=-limit; i<limit; i++) {
            assertTrue(flexSet.contains(new TestObject_0(i*capacity)));
        }
        assertTrue(flexSet.size() == 2*limit);
    }

    @Test
    public void shouldRemoveObjectsFromTheSameIdRef() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        int limit = 256;
        for (int i = 0; i<limit; i++) {
            flexSet.add(new TestObject_0(i*limit));
        }
        for (int i=0; i<limit; i++) {
            flexSet.remove(new TestObject_0(i*limit));
        }
        for (int i=0; i<limit; i++) {
            assertTrue(!flexSet.contains(new TestObject_0(i*limit)));
        }
    }

    @Test
    public void shouldAddObjectsEvenForConstantHashCode() {
        FlexSet<TestObject_5> flexSet = FlexSet.instance();
        int limit = flexSet.capacity;
        TestObject_5[] array = new TestObject_5[limit];
        for (int i=0; i<limit; i++) {
            array[i] = new TestObject_5(i);
        }
        for (TestObject_5 testObject_5 : array) {
            flexSet.add(testObject_5);
        }
        assertTrue(flexSet.size() == limit);
        for (TestObject_5 testObject_5 : array) {
            assertTrue(flexSet.contains(testObject_5));
        }
    }

    @Test
    public void shouldAddObjectsEvenForConstantIdHashCode() {
        FlexSet<TestObject_6> flexSet = FlexSet.instance();
        int limit = flexSet.capacity;
        TestObject_6[] array = new TestObject_6[limit];
        for (int i=0; i<limit; i++) {
            array[i] = new TestObject_6(i);
        }
        for (TestObject_6 testObject_6 : array) {
            flexSet.add(testObject_6);
        }
        assertTrue(flexSet.size() == limit);
        for (TestObject_6 testObject_6 : array) {
            assertTrue(flexSet.contains(testObject_6));
        }
    }

    @Test
    public void shouldShrinkOnRemoval() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i=0; i<129; i++) {
            flexSet.add(new TestObject_0(i));
        }
        int capacity = flexSet.capacity;
        for (int i=0; i<129; i++) {
            flexSet.remove(new TestObject_0(i));
        }
        assertTrue(flexSet.capacity != capacity);

        for (int i=0; i<129; i++) {
            flexSet.add(new TestObject_0(i));
        }
        Collection<TestObject_0> c = new ArrayList<>();
        for (int i=0; i<129; i++) {
            c.add(new TestObject_0(i));
        }
        flexSet.removeAll(c);
        assertTrue(flexSet.capacity != capacity);

    }

    @Test
    public void shouldCheckNecessaryConditionsWhenCallingContainsAllMethod() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        TestObject_0 object0 = new TestObject_0(0);
        TestObject_0 object1 = new TestObject_0(1);
        TestObject_0 object2 = new TestObject_0(2);
        flexSet.add(object0);
        flexSet.add(object1);
        boolean result = flexSet.containsAll(new HashSet<>(Arrays.asList(object0, object1, object2)));
        assertTrue(!result);
    }

    @Test
    public void shouldHandleNegativeHashCodes() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        TestObject_0 testObject = new TestObject_0(-983);
        flexSet.add(testObject);
        assertTrue(flexSet.size() == 1);
        assertTrue(flexSet.getByElem(testObject).getId() == -983);

    }

    @Test
    public void shouldThrowExceptionWhenTryingToAddNullElement() {
        exception.expect(NullPointerException.class);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.add(null);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToRemoveNullElement() {
        exception.expect(NullPointerException.class);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.remove(null);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToGetByNullElement() {
        exception.expect(NullPointerException.class);
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        flexSet.getByElem(null);
    }

    @Test
    public void shouldAdjustInitialCapacitySoThatItsAnIntEqualPowerOfTwo() {
        assertTrue(FlexSet.instance(64).capacity == 64);
        assertTrue(FlexSet.instance(63).capacity == 64);
        assertTrue(FlexSet.instance(32).capacity == 32);
        assertTrue(FlexSet.instance(31).capacity == 32);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToPassNullAsAnArgumentToAddAllMethod() {
        exception.expect(NullPointerException.class);
        FlexSet flexSet = FlexSet.instance();
        flexSet.addAll(null);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToPassNullAsAnArgumentToRemoveAllMethod() {
        exception.expect(NullPointerException.class);
        FlexSet flexSet = FlexSet.instance();
        flexSet.removeAll(null);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToPassNullAsAnArgumentToRemoveAllIdsMethod() {
        exception.expect(NullPointerException.class);
        FlexSet flexSet = FlexSet.instance();
        flexSet.removeAllIds(null);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToPassNullAsAnArgumentToRetainAllMethod() {
        exception.expect(NullPointerException.class);
        FlexSet flexSet = FlexSet.instance();
        flexSet.retainAll(null);
    }

    @Test
    public void shouldThrowExceptionWhenTryingToPassNullAsAnArgumentToRetainAllIdsMethod() {
        exception.expect(NullPointerException.class);
        FlexSet flexSet = FlexSet.instance();
        flexSet.retainAllIds(null);
    }

    @Test
    public void shouldBePossiblyUsedAsIdInAnotherFlexSet() {
        FlexSet<FlexSet<TestObject_0>> flexSet = FlexSet.instance();
        for (int i=0; i<3; i++) {
            FlexSet<TestObject_0> subFlexSet = FlexSet.instance();
            for (int j=0; j<3; j++) {
                subFlexSet.add(new TestObject_0(i+j));
            }
            flexSet.add(subFlexSet);
        }
        assertTrue(flexSet.size() == 3);
        for (FlexSet subFlexSet : flexSet) {
            assertTrue(subFlexSet.size() == 3);
        }
    }

    @Test
    public void shouldExpandOnAddition() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        int capacity = flexSet.capacity;
        int modCapacity = flexSet.modCapacity;
        int rebuildThreshold = flexSet.expansionThreshold;
        for (int i=0; i<rebuildThreshold+1;i++) {
            flexSet.add(new TestObject_0(i));
        }
        assertTrue(capacity == flexSet.capacity/2);
        assertTrue(modCapacity == (flexSet.modCapacity-1)/2);
        assertTrue(rebuildThreshold != flexSet.expansionThreshold);
    }

    @Test
    public void shouldTreeifyAndUntreeifyRefs() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i=0; i<FlexSet.ID_REF_TREEIFY_THRESHOLD; i++) {
            flexSet.add(new TestObject_0(i*1024));
        }
        assertTrue(flexSet.elements[0] instanceof FlexSet.TreeIdRef);
        for (int i=0; i<FlexSet.ID_REF_TREEIFY_THRESHOLD-FlexSet.ID_REF_UNTREEIFY_THRESHOLD; i++) {
            flexSet.remove(new TestObject_0(i*1024));
        }
        assertTrue(!(flexSet.elements[0] instanceof FlexSet.TreeIdRef));
    }


}
