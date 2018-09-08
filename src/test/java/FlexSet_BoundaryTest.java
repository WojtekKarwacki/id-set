import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

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
        for (int i=0; i<33; i++) {
            flexSet.add(new TestObject_0(i*capacity));
        }
        for (int i=0; i<33; i++) {
            assertTrue(flexSet.contains(new TestObject_0(i*capacity)));
        }
    }

    @Test
    public void shouldRetainCapacityOnRemovingMethods() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i=0; i<33; i++) {
            flexSet.add(new TestObject_0(i));
        }
        int capacity = flexSet.capacity;
        flexSet.clear();
        assertTrue(flexSet.capacity == capacity);

        for (int i=0; i<33; i++) {
            flexSet.add(new TestObject_0(i));
        }
        for (int i=0; i<33; i++) {
            flexSet.remove(new TestObject_0(i));
        }
        assertTrue(flexSet.capacity == capacity);

        for (int i=0; i<33; i++) {
            flexSet.add(new TestObject_0(i));
        }
        Collection<TestObject_0> c = new ArrayList<>();
        for (int i=0; i<33; i++) {
            c.add(new TestObject_0(i));
        }
        flexSet.removeAll(c);
        assertTrue(flexSet.capacity == capacity);

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
    public void capacityAndRebuildThresholdShouldChangeOnRebuild() {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        int capacity = flexSet.capacity;
        int modCapacity = flexSet.modCapacity;
        int rebuildThreshold = flexSet.rebuildThreshold;
        for (int i=0; i<rebuildThreshold+1;i++) {
            flexSet.add(new TestObject_0(i));
        }
        assertTrue(capacity == flexSet.capacity/2);
        assertTrue(modCapacity == (flexSet.modCapacity-1)/2);
        assertTrue(rebuildThreshold != flexSet.rebuildThreshold);
    }


}