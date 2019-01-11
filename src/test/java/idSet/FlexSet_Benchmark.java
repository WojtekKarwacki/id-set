package idSet;

import com.google.common.collect.Maps;
import jmh.JmhResultManager;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
//todo complex benchmark
@Warmup(iterations = 1, time = 200, timeUnit = MILLISECONDS)
@Measurement(iterations = 100, time = 20, timeUnit = MILLISECONDS)
@Fork(value = 5)
@State(Scope.Thread)
public class FlexSet_Benchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(new String[]{"-rf", "json"});
        JmhResultManager jmhResultManager = new JmhResultManager();
        jmhResultManager.handleResult();
    }

    private Object[] ids;
    private Identifiable[] testObjects;

    private HashMap<Object, Object> hashMap_add;
    private HashSet<Object> hashSet_add;
    private FlexSet<Identifiable> flexSet_add;

    private HashMap<Object, Object> hashMap_contains;
    private HashSet<Object> hashSet_contains;
    private FlexSet<Identifiable> flexSet_contains;

    private HashMap<Object, Object> hashMap_removeId;
    private FlexSet<Identifiable> flexSet_removeId;
    private HashSet<Object> hashSet_remove;
    private FlexSet<Identifiable> flexSet_remove;

    @Param({"1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096", "8192", "16384", "32768", "65536", "131072"})
    //@Param({"1", "2", "4", "8"})
    private int numberOfElements;

    @Param({"0", "1", "2", "3"})
    //@Param({"3"})
    private int function;

    //@Param({"0", "1"})
    @Param({"0", "1"})
    private int testObjectType;

    @Setup
    public void setUp() throws Exception {
        switch(testObjectType) {
            case 0:
                ids = new Integer[numberOfElements];
                testObjects = new TestObject_0[numberOfElements];
                break;
            case 1:
                ids = new ComplexTestObject.IntegerId[numberOfElements];
                testObjects = new ComplexTestObject[numberOfElements];
                break;
            default:
                throw new Exception();
        }
        hashMap_add = new HashMap<>();
        hashSet_add = new HashSet<>();
        flexSet_add = FlexSet.instance();
        hashMap_contains = new HashMap<>();
        hashSet_contains = new HashSet<>();
        flexSet_contains = FlexSet.instance();
        hashMap_removeId = new HashMap<>();
        flexSet_removeId = FlexSet.instance();
        hashSet_remove = new HashSet<>();
        flexSet_remove = FlexSet.instance();
        for (int i = 0; i < numberOfElements; i++) {
            int j;
            switch (function) {
                case 0:
                    j = i;
                    break;
                case 1:
                    j = i*i;
                    break;
                case 2:
                    j = ((i%2)*2-1)*i/2;
                    break;
                case 3:
                    j = i*4096;
                    break;
                default:
                    throw new Exception();
            }
            Identifiable testObject;
            switch(testObjectType) {
                case 0:
                    ids[i] = j;
                    testObject = new TestObject_0(j);
                    break;
                case 1:
                    ids[i] = new ComplexTestObject.IntegerId(j);
                    testObject = new ComplexTestObject(new ComplexTestObject.IntegerId(j));
                    break;
                default:
                    throw new Exception();
            }
            testObjects[i] = testObject;
            hashMap_contains.put(new ComplexTestObject.IntegerId(j), testObject);
            hashSet_contains.add(testObject);
            flexSet_contains.add(testObject);
            hashMap_removeId.put(new ComplexTestObject.IntegerId(j), testObject);
            hashSet_remove.add(testObject);
            flexSet_removeId.add(testObject);
            flexSet_remove.add(testObject);
        }
    }

    @Benchmark
    public void timeAdd_HashMap(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashMap_add.put(ids[i], testObjects[i]));
        }
    }

    @Benchmark
    public void timeAdd_HashSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashSet_add.add(testObjects[i]));
        }
    }

    @Benchmark
    public void timeAdd_FlexSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(flexSet_add.add(testObjects[i]));
        }
    }

    @Benchmark
    public void timeContainsKey_HashMap(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashMap_contains.containsKey(ids[i]));
        }
    }

    @Benchmark
    public void timeContainsKey_FlexSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(flexSet_contains.containsId(ids[i]));
        }
    }

    @Benchmark
    public void timeContainsValue_HashSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashSet_contains.contains(testObjects[i]));
        }
    }

    @Benchmark
    public void timeContainsValue_FlexSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(flexSet_contains.contains(testObjects[i]));
        }
    }

    @Benchmark
    public void timeRemoveId_HashMap(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashMap_removeId.remove(ids[i]));
        }
    }

    @Benchmark
    public void timeRemoveId_FlexSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(flexSet_removeId.removeId(ids[i]));
        }
    }

    @Benchmark
    public void timeRemoveValue_HashSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(hashSet_remove.remove(testObjects[i]));
        }
    }

    @Benchmark
    public void timeRemoveValue_FlexSet(Blackhole bh) {
        for (int i = 0; i < numberOfElements; i++) {
            bh.consume(flexSet_remove.removeId(testObjects[i]));
        }
    }

}
