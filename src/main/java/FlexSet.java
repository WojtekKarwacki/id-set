import com.sun.istack.internal.NotNull;

import java.lang.reflect.Array;
import java.util.*;



public class FlexSet<E extends Identifiable> implements IdSet<E>, Identifiable {

    private static final int MAX_CAPACITY = Integer.MAX_VALUE;
    private static final int DEFAULT_INITIAL_CAPACITY = 1024;

    private IdRef<E>[] elements;
    private int size;

    int capacity;
    int modCapacity;
    int rebuildThreshold;


    private FlexSet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Parameter initialCapacity should be greater than 0.");
        }
        size = 0;
        capacity = initialCapacity;
        calculateModCapacity();
        calculateRebuildThreshold();
        elements = initElements();
    }


    public static <T extends Identifiable> FlexSet<T> instance() {
        return instance(DEFAULT_INITIAL_CAPACITY);
    }

    public static <T extends Identifiable> FlexSet<T> instance(int initialCapacity) {
        int highestOneBit = Integer.highestOneBit(initialCapacity);
        initialCapacity = initialCapacity == highestOneBit ? initialCapacity : highestOneBit << 1;
        return new FlexSet<>(initialCapacity);
    }

    @SafeVarargs
    public static <T extends Identifiable> FlexSet fromArray(T... a) {
        FlexSet<T> flexSet = instance(a.length);
        Collections.addAll(flexSet, a);
        return flexSet;
    }

    public static <K ,V> FlexSet<IdWrapper<K, V>> fromMap(Map<K ,V> map) {
        Objects.requireNonNull(map);
        FlexSet<IdWrapper<K, V>> flexSet = instance(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            flexSet.add(new IdWrapper<>(entry.getKey(), entry.getValue()));
        }
        return flexSet;
    }

    private IdRef<E>[] initElements() {
        @SuppressWarnings("unchecked")
        IdRef<E>[] elements = (IdRef<E>[]) Array.newInstance(IdRef.class, capacity);
        for (int i=0; i<elements.length; i++) {
            elements[i] = IdRef.getEmpty();
        }
        return elements;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return containsId(ensureTypeValid(o).getId());
    }

    @Override
    public boolean containsId(Object o) {
        return get(o) != null;
    }

    @Override
    public E getByElem(E e) {
        Objects.requireNonNull(e);
        return get(e.getId());
    }

    @Override
    public E get(Object id) {
        int hashCode = id.hashCode();
        IdRef<E> idRef = elements[modHashCode(hashCode)];
        return idRef.get(id, hashCode);
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        return copyToArray(a);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[]  toArray(T[] a) {
        Objects.requireNonNull(a);
        ensureCapacityOfGivenArray(a);
        a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        return copyToArray(a);
    }

    @SuppressWarnings("unchecked")
    public <K> Map<K, E> toHashMap() {
        Map<K, E> map = new HashMap<>();
        for (E e: this) {
            map.put((K) e.getId(), e);
        }
        return map;
    }

    private <T> void ensureCapacityOfGivenArray(T[] a) {
        if (a.length < size) {
            throw new IllegalArgumentException(String.format("Cannot fit FlexSet elements into array of length %s.", a.length));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T[] copyToArray(T[] a) {
        int i = 0;
        for (E e : this) {
            a[i] = (T) e;
            i++;
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        Objects.requireNonNull(e);
        int hashCode = e.getId().hashCode();
        IdRef<E> idRef = elements[modHashCode(hashCode)];
        if (idRef.add(e, hashCode)) {
            adjustOrRebuildOnAdd();
            return true;
        }
        return false;
    }

    private void adjustOrRebuildOnAdd() {
        if (++size > rebuildThreshold && size < MAX_CAPACITY) {
            rebuild();
        }
    }

    private void rebuild() {
        capacity <<= 1;
        calculateModCapacity();
        calculateRebuildThreshold();
        this.elements = rebuildElements();
    }

    private IdRef<E>[] rebuildElements() {
        IdRef<E>[] elements = initElements();
        for (int i=0; i<this.elements.length; i++) {
            IdRef<E> idRef = this.elements[i];
            rebuildElement(elements, idRef);
        }
        return elements;
    }

    private void rebuildElement(IdRef<E>[] elements, IdRef<E> idRef) {
        while (idRef.e != null) {
            int hashCode = idRef.hashCode;
            elements[modHashCode(hashCode)].add(idRef.e, hashCode);
            idRef = idRef.next;
        }
    }

    @Override
    public boolean remove(Object o) {
        Objects.requireNonNull(o);
        return removeId(ensureTypeValid(o).getId()) != null;
    }

    @Override
    public E removeId(Object id) {
        int hashCode = id.hashCode();
        IdRef<E> idRef = elements[modHashCode(hashCode)];
        E e = idRef.removeId(id, hashCode);
        if (e != null) {
            size--;
        }
        return e;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (checkNecessaryConditionsForContainsAll(c)) {
            return false;
        }
        for (Object o : c) {
            if (!(o instanceof Identifiable) || !containsId(((Identifiable) o).getId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAllIds(Collection<?> c) {
        if (checkNecessaryConditionsForContainsAll(c)) {
            return false;
        }
        for (Object o : c) {
            if (!containsId(o)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkNecessaryConditionsForContainsAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return c instanceof Set && size < c.size();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Objects.requireNonNull(c);
        boolean result = false;
        for (E e : c) {
            if (add(e)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        Objects.requireNonNull(c);
        boolean result = false;
        for (Object o : c) {
            if (o instanceof Identifiable && removeId(((Identifiable) o).getId()) != null) {
                result = true;
            }

        }
        return result;
    }

    @Override
    public boolean removeAllIds(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean result = false;
        for (Object o : c) {
            if (removeId(o) != null) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean result = false;
        for (E e : this) {
            if (!c.contains(e)) {
                if (remove(e)) {
                    result = true;
                }

            }
        }
        return result;
    }

    @Override
    public boolean retainAllIds(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean result = false;
        for (E e : this) {
            Object id = e.getId();
            if (!c.contains(id)) {
                if (removeId(id) != null) {
                    result = true;
                }

            }
        }
        return result;
    }

    @Override
    public void clear() {
        size = 0;
        elements = initElements();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private IdRef<E> current;
            private int index;

            @Override
            public boolean hasNext() {
                while (index < elements.length) {
                    if (current == null) {
                        current = elements[index];
                    }
                    if (current.e != null) {
                        return true;
                    }
                    current = null;
                    index++;
                }
                return false;
            }

            @Override
            public E next() {
                E result = current.e;
                current = current.next;
                return result;
            }

        };
    }

    @Override
    public Set<Object> idSet() {
        Set<Object> idSet = new HashSet<>();
        for (E e: this) {
            idSet.add(e.getId());
        }
        return idSet;
    }

    @Override
    public Set<E> entrySet() {
        return new HashSet<>(this);
    }

    private int modHashCode(int hashCode) {
        return hashCode & modCapacity;
    }

    private void calculateModCapacity() {
        modCapacity = capacity-1;
    }

    private void calculateRebuildThreshold() {
        rebuildThreshold = (capacity >> 4) * 6 ;
    }

    private Identifiable ensureTypeValid(Object o) {
        if (o instanceof Identifiable) {
            return (Identifiable) o;
        }
        throw new IllegalArgumentException(String.format("Object %s has to be of type Identifiable.", o));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlexSet<?> flexSet = (FlexSet<?>) o;
        return size == flexSet.size &&
                containsAll(flexSet);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(capacity, size);
        result = 31 * result + Arrays.hashCode(elements);
        return result;
    }

    @Override
    public Object getId() {
        return hashCode();
    }

    @Override
    public String toString() {
        return "FlexSet{" +
                "elements=" + Arrays.toString(elements) +
                '}';
    }

    private final static class IdRef<E extends Identifiable> {


        private E e;
        private IdRef<E> next;
        private int hashCode;

        private IdRef() {
        }

        private boolean add(E e, int hashCode) {
            if (e.equals(this.e)) {
                return false;
            }
            if (this.e == null) {
                setUpAtTheEnd(this, e, hashCode);
                return true;
            }
            IdRef<E> current = this;
            while (current.hashCode < hashCode) {
                if (xxx(e, hashCode, current)) return true;
                current = current.next;
            }
            while (current.e != null) {
                if (current.hashCode != hashCode) {
                    setUpInTheMiddle(current, e, hashCode);
                    return true;
                }
                if (current.e.equals(e)) {
                    return false;
                }
                current = current.next;
            }
            setUpAtTheEnd(current, e, hashCode);
            return true;
        }

        private boolean xxx(E e, int hashCode, IdRef<E> current) {
            if (current.next == null) {
                setUpAtTheEnd(current, e, hashCode);
                return true;
            }
            return false;
        }

        private void setUpAtTheEnd(IdRef<E> current, E e, int hashCode) {
            current.e = e;
            current.next = getEmpty();
            current.hashCode = hashCode;
        }

        private void setUpInTheMiddle(IdRef<E> current, E e, int hashCode) {
            IdRef<E> idRef = getEmpty();
            idRef.e = e;
            idRef.next = current.next;
            idRef.hashCode = hashCode;
            current.next = idRef;
        }

        private E removeId(Object id, int hashCode) {
            if (proceedWithNullWhenEmpty(IdRef.this)) return null;
            IdRef<E> current = this;
            while (current.hashCode < hashCode) {
                if (proceedWithNullWhenEmpty(current)) return null;
                current = current.next;
            }
            while (current.e != null && current.hashCode == hashCode) {
                if (getFound(current, id)) return getRemovedAndAdjust(current);
                current = current.next;
            }
            return null;
        }

        private E getRemovedAndAdjust(IdRef<E> current) {
            IdRef<E> next = current.next;
            E e = current.e;
            adjustOnRemoval(current, next);
            return e;
        }

        private void adjustOnRemoval(IdRef<E> current, IdRef<E> next) {
            current.e = next.e;
            current.next = next.next;
            current.hashCode = next.hashCode;
        }

        public E get(Object id, int hashCode) {
            if (proceedWithNullWhenEmpty(IdRef.this)) return null;
            IdRef<E> current = this;
            while (current.hashCode < hashCode) {
                if (proceedWithNullWhenEmpty(current)) return null;
                current = current.next;
            }
            while (current.e != null && current.hashCode == hashCode) {
                if (getFound(current, id)) return current.e;
                current = current.next;
            }
            return null;
        }

        private boolean getFound(IdRef<E> current, Object id) {
            if (current.e.getId().equals(id)) {
                return true;
            }
            return false;
        }

        private boolean proceedWithNullWhenEmpty(IdRef<E> current) {
            if (current.e == null) {
                return true;
            }
            return false;
        }

        public E getElem() {
            return e;
        }

        public Object getId() {
            return e.getId();
        }

        private static <E extends Identifiable> IdRef<E> getEmpty() {
            return new IdRef<>();
        }

        @Override
        public String toString() {
            return "IdRef{" +
                    "e=" + e +
                    ", next=" + next +
                    ", hashCode=" + hashCode +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdRef<?> idRef = (IdRef<?>) o;
            return hashCode == idRef.hashCode &&
                    Objects.equals(e, idRef.e);
        }

        @Override
        public int hashCode() {
            return Objects.hash(e, hashCode);
        }
    }
}
