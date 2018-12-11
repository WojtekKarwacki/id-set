package idSet;

import idSet.FlexSet;
import idSet.TestObject_0;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 1, time = 400, timeUnit = MILLISECONDS)
@Measurement(iterations = 3, time = 200, timeUnit = MILLISECONDS)
@Fork(value = 2)
@State(Scope.Thread)
public class FlexSet_AddingBenchmark{

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    private TestObject_0[] array;

    private HashMap<Integer, TestObject_0> hashMap;
    private HashSet<TestObject_0> hashSet;
    private FlexSet<TestObject_0> flexSet;

    //@Param({"1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096", "8192", "16384", "32768", "65536", "131072"})
    @Param({"4"})
    private int numberOfElements;

    //@Param({"0", "1", "2"})
    @Param({"3"})
    private int function;


    @Setup
    public void setUp() throws Exception {
        hashMap = new HashMap<>();
        hashSet = new HashSet<>();
        flexSet = FlexSet.instance();
        array = new TestObject_0[numberOfElements];
        for (int i = 0; i < numberOfElements; i++) {
            TestObject_0 testObject;
            switch (function) {
                case 0:
                    testObject = new TestObject_0(i);
                    break;
                case 1:
                    testObject = new TestObject_0(i*i);
                    break;
                case 2:
                    testObject = new TestObject_0(((i%2)*2-1)*i/2);
                    break;
                case 3:
                    testObject = new TestObject_0((i*4096));
                    break;
                default:
                    throw new Exception();
            }
            array[i] = testObject;
        }
    }


    @Benchmark
    public void timeHashMap(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashMap.put(i, array[i]));
        }
    }

    @Benchmark
    public void timeHashSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashSet.add(array[i]));
        }
    }

    @Benchmark
    public void timeFlexSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(flexSet.add(array[i]));
        }
    }

}
