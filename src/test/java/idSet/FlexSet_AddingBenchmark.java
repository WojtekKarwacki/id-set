package idSet;

import idSet.FlexSet;
import idSet.TestObject_0;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 1, time = 500, timeUnit = MILLISECONDS)
@Measurement(iterations = 1, time = 500, timeUnit = MILLISECONDS)
@State(Scope.Thread)
public class FlexSet_AddingBenchmark{

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
    private final int NUMBER_OF_ELEMENTS = 511;
    private final TestObject_0[] array = new TestObject_0[NUMBER_OF_ELEMENTS];
    {
        for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
            array[i] = new TestObject_0(i*i);
        }
    }


    @Benchmark
    public void timeHashMap(Blackhole bh) {
        HashMap<Integer, TestObject_0> hashMap = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
            bh.consume(hashMap.put(i, array[i]));
        }
    }

    @Benchmark
    public void timeHashSet(Blackhole bh) {
        HashSet<TestObject_0> hashSet = new HashSet<>();
        for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
            bh.consume(hashSet.add(array[i]));
        }
    }

/*    @Benchmark
    public List<idSet.TestObject_0> timeArrayList(StateClass stateClass) {
        List<idSet.TestObject_0> arrayList = this.arrayList;
        for (int i = 0; i < reps; i++) {
            arrayList.add(array[i]);
        }
        return arrayList;
    }*/

    @Benchmark
    public void timeFlexSet(Blackhole bh) {
        FlexSet<TestObject_0> flexSet = FlexSet.instance();
        for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
            bh.consume(flexSet.add(array[i]));
        }
    }


}
