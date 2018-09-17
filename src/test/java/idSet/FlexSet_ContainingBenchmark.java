package idSet;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashMap;
import java.util.HashSet;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 1, time = 4000, timeUnit = MILLISECONDS)
@Measurement(iterations = 3, time = 2000, timeUnit = MILLISECONDS)
@Fork(value = 2)
@State(Scope.Thread)
public class FlexSet_ContainingBenchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    private int[] keysArray;
    private TestObject_0[] objectsArray;

    private HashMap<Integer, TestObject_0> hashMap;
    private HashSet<TestObject_0> hashSet;
    private FlexSet<TestObject_0> flexSet;

    //@Param({"1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096", "8192", "16384", "32768", "65536", "131072"})
    @Param({"256"})
    private int numberOfElements;

    @Setup
    public void setUp() {
        hashMap = new HashMap<>();
        hashSet = new HashSet<>();
        flexSet = FlexSet.instance();
        keysArray = new int[numberOfElements];
        objectsArray = new TestObject_0[numberOfElements];
        for (int j = 0; j < numberOfElements; j++) {
            TestObject_0 testObject = new TestObject_0(j);
            keysArray[j] = j;
            objectsArray[j] = testObject;
            hashMap.put(j, testObject);
            hashSet.add(testObject);
            flexSet.add(testObject);
        }
    }

/*    @Benchmark
    public void timeHashMap(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashMap.get(keysArray[i]));
        }
    }*/

    @Benchmark
    public void timeHashSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashSet.contains(objectsArray[i]));
        }
    }

    @Benchmark
    public void timeFlexSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(flexSet.contains(objectsArray[i]));
        }
    }

}
