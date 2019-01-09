package idSet;

import java.util.Arrays;

public class CuckooSet {
    private int[] keysT1;
    private int[] keysT2;
    private int tableLength;
    private int hashShift;
    private int rehashCounter = 0;

    private double loadFactor;
    private int capacity;
    private int insertRounds;

    private static final long H1 = 2654435761L;
    private static final long H2 = 0x6b43a9b5L;
    private static final long INT_MASK = 0x07FFFFFFF;

    private static final int FREE = -1;

    public CuckooSet(int capacity, double loadFactor, int insertRounds) {
        this.loadFactor = loadFactor;
        this.insertRounds = insertRounds;

        initializeCapacity(capacity, loadFactor);
    }

    public boolean add(int key) {
        if (contains(key)) {
            return false;
        }
        for (int i = 0; i < insertRounds; i++) {
            int h1 = h1(key);
            int register = keysT1[h1];
            keysT1[h1] = key;
            if (register == FREE) {
                return true;
            }
            key = register;

            int h2 = h2(key);
            register = keysT2[h2];
            keysT2[h2] = key;
            if (register == FREE) {
                return true;
            }
            key = register;
        }

        rehash();

        return add(key);
    }

    public boolean contains(int key) {
        return keysT1[h1(key)] == key || keysT2[h2(key)] == key;
    }

    public int getRehashCounter() {
        return rehashCounter;
    }

    private void rehash() {
        int[] oldT1 = keysT1;
        int[] oldT2 = keysT2;

        int oldTableLength = tableLength;
        initializeCapacity(capacity * 2, loadFactor);

        for (int i = 0; i < oldTableLength; i++) {
            if (oldT1[i] != FREE) {
                add(oldT1[i]);
            }
            if (oldT2[i] != FREE) {
                add(oldT2[i]);
            }
        }

        rehashCounter++;
    }

    private void initializeCapacity(int capacity, double loadFactor) {
        this.capacity = capacity;
        tableLength = (int) (capacity / loadFactor);
        hashShift = 32 - (int) Math.ceil(Math.log(tableLength) / Math.log(2));

        keysT1 = new int[tableLength];
        keysT2 = new int[tableLength];

        Arrays.fill(keysT1, FREE);
        Arrays.fill(keysT2, FREE);
    }

    private int h1(int key) {
        return ((int) (((int) (key * H1) >> hashShift) & INT_MASK)) % tableLength;
    }

    private int h2(int key) {
        return ((int) (((int) (key * H2) >> hashShift) & INT_MASK)) % tableLength;
    }
}