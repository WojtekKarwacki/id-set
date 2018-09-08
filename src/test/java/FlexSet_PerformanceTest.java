import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;

import java.util.*;

public class FlexSet_PerformanceTest {

    public static class AddingBenchmark extends SimpleBenchmark {

        public static final int NUMBER_OF_ELEMENTS = 128;
        private static final int NUMBER_OF_ELEMENTS_MINUS_ONE = NUMBER_OF_ELEMENTS - 1;

        HashMap<Integer, TestObject_0> hashMap;
        HashSet<TestObject_0> hashSet;
        ArrayList<TestObject_0> arrayList;
        FlexSet<TestObject_0> flexSet;
        TestObject_0[] array = new TestObject_0[NUMBER_OF_ELEMENTS];

        @Override
        protected void setUp() {
            hashMap = new HashMap<>();
            hashSet = new HashSet<>();
            arrayList = new ArrayList<>();
            flexSet = FlexSet.instance();
            for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
                array[i] = new TestObject_0((i));
            }
        }

/*        public Map<Integer, TestObject_0> timeHashMap(int reps) {
            for (int i = 0; i < reps; i++) {
                hashMap.put(i, array[i & NUMBER_OF_ELEMENTS_MINUS_ONE]);
            }
            return hashMap;
        }*/

        public Map<Integer, TestObject_0> timeHashSet(int reps) {
            for (int i = 0; i < reps; i++) {
                hashSet.add(array[i & NUMBER_OF_ELEMENTS_MINUS_ONE]);
            }
            return hashMap;
        }

        public Map<Integer, TestObject_0> timeArrayList(int reps) {
            for (int i = 0; i < reps; i++) {
                arrayList.add(array[i & NUMBER_OF_ELEMENTS_MINUS_ONE]);
            }
            return hashMap;
        }

        public Set<TestObject_0> timeFlexSet(int reps) {
            for (int i = 0; i < reps; i++) {
                flexSet.add(array[i & NUMBER_OF_ELEMENTS_MINUS_ONE]);
            }
            return flexSet;
        }


    }

    /*public static class GettingBenchmark extends SimpleBenchmark {

        private static final int NUMBER_OF_ELEMENTS = 131072;
        private static final int NUMBER_OF_ELEMENTS_MINUS_ONE = NUMBER_OF_ELEMENTS - 1;

        HashMap<TestObject_3.TestId, TestObject_3> hashMap;
        TestObject_3[] hashMapArray = new TestObject_3[NUMBER_OF_ELEMENTS];
        FlexSet<TestObject_3> flexSet;
        TestObject_3[] flexSetArray = new TestObject_3[NUMBER_OF_ELEMENTS];

        TestObject_3.TestId[] keysArray = new TestObject_3.TestId[NUMBER_OF_ELEMENTS];

        @Override
        protected void setUp() {
            hashMap = new HashMap<>();
            flexSet = FlexSet.instance();
            for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
                TestObject_3 testObject = new TestObject_3(i);
                hashMap.put(new TestObject_3.TestId(i), testObject);
                flexSet.add(testObject);
                keysArray[i] = new TestObject_3.TestId(i);
            }
        }

        public TestObject_3[] timeHashMap(int reps) {
            for (int i = 0; i < reps; i++) {
                int index = i & NUMBER_OF_ELEMENTS_MINUS_ONE;
                hashMapArray[index] = hashMap.get(keysArray[index]);
            }
            return hashMapArray;
        }



        public TestObject_3[] timeFlexSet(int reps) {
            for (int i = 0; i < reps; i++) {
                flexSetArray[i & NUMBER_OF_ELEMENTS_MINUS_ONE] = flexSet.get(i);
            }
            return flexSetArray;
        }

    }

    public static class GettingBenchmarkWithKeyCreation extends SimpleBenchmark {

        private static final int NUMBER_OF_ELEMENTS = 131072;
        private static final int NUMBER_OF_ELEMENTS_MINUS_ONE = NUMBER_OF_ELEMENTS - 1;

        HashMap<TestObject_3.TestId, TestObject_3> hashMap;
        TestObject_3[] hashMapArray = new TestObject_3[NUMBER_OF_ELEMENTS];
        FlexSet<TestObject_3> flexSet;
        TestObject_3[] flexSetArray = new TestObject_3[NUMBER_OF_ELEMENTS];

        @Override
        protected void setUp() {
            hashMap = new HashMap<>();
            flexSet = FlexSet.instance();
            for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
                TestObject_3 testObject = new TestObject_3(i);
                hashMap.put(new TestObject_3.TestId(i), testObject);
                flexSet.add(testObject);
            }
        }

        public TestObject_3[] timeHashMap(int reps) {
            for (int i = 0; i < reps; i++) {
                hashMapArray[i & NUMBER_OF_ELEMENTS_MINUS_ONE] = hashMap.get(new TestObject_3.TestId(i));
            }
            return hashMapArray;
        }



        public TestObject_3[] timeFlexSet(int reps) {
            for (int i = 0; i < reps; i++) {
                flexSetArray[i & NUMBER_OF_ELEMENTS_MINUS_ONE] = flexSet.get(i);
            }
            return flexSetArray;
        }
    }

    public static class RemovingBenchmark extends SimpleBenchmark {

        private static final int NUMBER_OF_ELEMENTS = 131072;
        private static final int NUMBER_OF_ELEMENTS_MINUS_ONE = NUMBER_OF_ELEMENTS - 1;

        HashMap<TestObject_3.TestId, TestObject_3> hashMap;
        TestObject_3[] hashMapArray = new TestObject_3[NUMBER_OF_ELEMENTS];
        FlexSet<TestObject_3> flexSet;
        TestObject_3[] flexSetArray = new TestObject_3[NUMBER_OF_ELEMENTS];

        TestObject_3.TestId[] keysArray = new TestObject_3.TestId[NUMBER_OF_ELEMENTS];

        @Override
        protected void setUp() {
            hashMap = new HashMap<>();
            flexSet = FlexSet.instance();
            for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
                TestObject_3 testObject = new TestObject_3(i);
                TestObject_3.TestId testId = new TestObject_3.TestId(i);
                hashMap.put(testId, testObject);
                flexSet.add(testObject);
                keysArray[i] = testId;
            }
        }

        public TestObject_3[] timeHashMap(int reps) {
            boolean result = true;
            for (int i = 0; i < reps; i++) {
                int index = i & NUMBER_OF_ELEMENTS_MINUS_ONE;
                hashMapArray[index] = hashMap.remove(keysArray[index]);
            }
            return hashMapArray;
        }



        public boolean timeFlexSet(int reps) {
            boolean result = true;
            for (int i = 0; i < reps; i++) {
                int index = i & NUMBER_OF_ELEMENTS_MINUS_ONE;
                flexSetArray[index] = flexSet.removeId(index);
            }
            return result;
        }

    }
*/
    /*public static class GettingBenchmarkWithKeyCreation extends SimpleBenchmark {

        private static final int NUMBER_OF_ELEMENTS = 131072;
        private static final int NUMBER_OF_ELEMENTS_MINUS_ONE = NUMBER_OF_ELEMENTS - 1;

        HashMap<TestObject_3.TestId, TestObject_3> hashMap;
        TestObject_3[] hashMapArray = new TestObject_3[NUMBER_OF_ELEMENTS];
        FlexSet<TestObject_3> flexSet;
        TestObject_3[] flexSetArray = new TestObject_3[NUMBER_OF_ELEMENTS];

        @Override
        protected void setUp() {
            hashMap = new HashMap<>();
            flexSet = FlexSet.instance();
            for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
                TestObject_3 testObject = new TestObject_3(i);
                hashMap.put(new TestObject_3.TestId(i), testObject);
                flexSet.add(testObject);
            }
        }

        public TestObject_3[] timeHashMap(int reps) {
            for (int i = 0; i < reps; i++) {
                hashMapArray[i & NUMBER_OF_ELEMENTS_MINUS_ONE] = hashMap.get(new TestObject_3.TestId(i));
            }
            return hashMapArray;
        }



        public TestObject_3[] timeFlexSet(int reps) {
            for (int i = 0; i < reps; i++) {
                flexSetArray[i & NUMBER_OF_ELEMENTS_MINUS_ONE] = flexSet.get(i);
            }
            return flexSetArray;
        }
    }*/

    public static void main(String... args) {
        Runner.main(AddingBenchmark.class, new String[0]);
    }
}
