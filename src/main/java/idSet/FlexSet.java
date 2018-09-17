package idSet;

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
            throw new IllegalArgumentException(String.format("Cannot fit idSet.FlexSet elements into array of length %s.", a.length));
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
            if (idRef.size == 7) { //todo do stalej, todo czy ++size czy size++
                elements[modHashCode(hashCode)] = TreeIdRef.fromIdRef(idRef); //todo ponowne wyliczenie modhashcode
            }
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
    public boolean removeAll(Collection<?> c) {
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
                    if (hasNextForCurrent()) {
                        return true;
                    }
                    prepareForNext();
                }
                return false;
            }

            private boolean hasNextForCurrent() {
                if (current == null) {
                    current = elements[index];
                }
                return current.e != null;
            }

            private void prepareForNext() {
                current = null;
                index++;
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
        throw new IllegalArgumentException(String.format("Object %s has to be of type idSet.Identifiable.", o));
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
        return "idSet.FlexSet{" +
                "elements=" + Arrays.toString(elements) +
                '}';
    }

    private static class IdRef<E extends Identifiable> {

        private int size;

        E e;
        IdRef<E> next;
        int hashCode;

        private IdRef() {
        }

        public E getElem() {
            return e;
        }

        boolean add(E e, int hashCode) {
            if (checkFirst(e) && (setUpIfEmpty(e, hashCode) || setUpAtTheBegginingIfNeeded(e, hashCode) || skipLowerHashCodesAndProceedWithAdding(e, hashCode))) {
                size++;
                return true;
            }
            return false;
        }

        private boolean checkFirst(E e) {
            return !e.equals(this.e);
        }

        private boolean setUpIfEmpty(E e, int hashCode) {
            if (this.e == null) {
                setUpAtTheEnd(this, e, hashCode);
                return true;
            }
            return false;
        }

        private boolean setUpAtTheBegginingIfNeeded(E e, int hashCode) {
            if (isHashCodeInRange(hashCode)) {
                setUpAtTheBeggining(e, hashCode);
                return true;
            }
            return false;
        }

        private void setUpAtTheBeggining(E e, int hashCode) {
            IdRef<E> thisTemp = getEmpty();
            thisTemp.e = this.e;
            thisTemp.next = next;
            thisTemp.hashCode = this.hashCode;
            this.e = e;
            next = thisTemp;
            this.hashCode = hashCode;
        }

        private boolean skipLowerHashCodesAndProceedWithAdding(E e, int hashCode) {
            IdRef<E> current = this;
            while (current.hashCode < hashCode) {
                if (setUpAtTheEndIfNeeded(e, hashCode, current)) return true;
                current = current.next;
            }
            while (current.e != null) {
                Boolean x = setUpInTheMiddleIfNeeded(current, e, hashCode);
                if (x != null) return x;
                current = current.next;
            }
            setUpAtTheEnd(current, e, hashCode);
            return true;
        }

        private boolean setUpAtTheEndIfNeeded(E e, int hashCode, IdRef<E> current) {
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

        private Boolean setUpInTheMiddleIfNeeded(IdRef<E> current, E e, int hashCode) {
            if (current.hashCode != hashCode) {
                setUpInTheMiddle(current, e, hashCode);
                return true;
            }
            if (current.e.equals(e)) {
                return false;
            }
            return null;
        }

        private void setUpInTheMiddle(IdRef<E> current, E e, int hashCode) {
            IdRef<E> idRef = getEmpty();
            idRef.e = e;
            idRef.next = current.next;
            idRef.hashCode = hashCode;
            current.next = idRef;
        }

        E get(Object id, int hashCode) {
            if (checkFirst(id)) {
                return e;
            }
            if (isHashCodeInRange(hashCode)) {
                return null;
            }
            return skipLowerHashCodesAndProceedWithGetting(id, hashCode);
        }

        private boolean checkFirst(Object id) {
            return e == null || e.getId().equals(id);
        }

        private boolean isHashCodeInRange(int hashCode) {
            return this.hashCode > hashCode;
        }

        private E skipLowerHashCodesAndProceedWithGetting(Object id, int hashCode) {
            return skipLowerHashCodesAndProceedWithGettingOrRemoving(id, hashCode, true);
        }

        E removeId(Object id, int hashCode) { //todo size--
            if (e == null) return null;
            if (e.getId().equals(id)) {
                return getRemovedAndAdjust(this);
            }
            return skipLowerHashCodesAndProceedWithRemoving(id, hashCode);
        }

        private E skipLowerHashCodesAndProceedWithRemoving(Object id, int hashCode) {
            return skipLowerHashCodesAndProceedWithGettingOrRemoving(id, hashCode, false);
        }

        private void adjustOnRemoval(IdRef<E> current, IdRef<E> next) {
            current.e = next.e;
            current.next = next.next;
            current.hashCode = next.hashCode;
        }

        private E skipLowerHashCodesAndProceedWithGettingOrRemoving(Object id, int hashCode, boolean getting) {
            IdRef<E> current = skipLowerHashCodes(hashCode);
            if (current == null) return null;
            while (current.e != null && current.hashCode == hashCode) {
                if (isFound(current, id)) return getting ? current.e : getRemovedAndAdjust(current);
                current = current.next;
            }
            return null;
        }

        private IdRef<E> skipLowerHashCodes(int hashCode) {
            IdRef<E> current = next;
            while (current.hashCode < hashCode) {
                if (current.e == null) return null;
                current = current.next;
            }
            return current;
        }

        private E getRemovedAndAdjust(IdRef<E> current) {
            IdRef<E> next = current.next;
            E e = current.e;
            adjustOnRemoval(current, next);
            return e;
        }

        private boolean isFound(IdRef<E> current, Object id) {
            return current.e.getId().equals(id);
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

    private final static class TreeIdRef<E extends Identifiable> extends IdRef<E> implements Identifiable {

        private TreeIdRef<E> left;
        private TreeIdRef<E> right;

        private FlexSet<E> block = FlexSet.instance(); //todo jaki instance?

        private TreeIdRef() {
        }

        @Override
        boolean add(E e, int hashCode) {
            TreeIdRef<E> treeIdRef = addByHashCode(this, e, hashCode);
            if (treeIdRef != null) {
                if (treeIdRef.block.add(e)) {//todo ponowne liczenie hashcode wewnatrz, moze optymalizacja?
                    next = treeIdRef;
                    this.e = e;
                    this.hashCode = hashCode;
                    return true;
                } else {
                    return false;
                }
            }
            return true;

        }

        private TreeIdRef<E> addByHashCode(TreeIdRef<E> current, E e, int hashCode) {
            TreeIdRef<E> previous = current;
            //todo uproszczenie + refaktor + optymalizacja
            if (hashCode < current.hashCode) {
                current = current.left;
                if (current == null) {
                    current = new TreeIdRef<>();
                    current.e = e;
                    current.hashCode = hashCode;
                    previous.left = current;
                    return null;
                }
            } else if (hashCode > current.hashCode) {
                current = current.right;
                if (current == null) {
                    current = new TreeIdRef<>();
                    current.e = e;
                    current.hashCode = hashCode;
                    previous.right = current;
                    return null;
                }
            } else {
                return current;
            }
            return addByHashCode(current, e, hashCode);
        }

        @Override
        E get(Object id, int hashCode) {
            TreeIdRef<E> treeIdRef = findByHashCode(this, hashCode);
            if (treeIdRef != null) {
                if (treeIdRef.e.getId().equals(id)) {
                    return treeIdRef.e;
                }
                return block.get(id);
            }
            return null;
        }

        private TreeIdRef<E> findByHashCode(TreeIdRef<E> current, int hashCode) {
            TreeIdRef<E> previous = current;
            if (hashCode < current.hashCode) {
                current = current.left;
                if (current == null) {
                    return null;
                }
            } else if (hashCode > current.hashCode) {
                current = current.right;
                if (current == null) {
                    return null;
                }
            } else {
                return current;
            }
            return findByHashCode(current, hashCode);
        }

        @Override
        E removeId(Object id, int hashCode) {
            return null;
        }

        @Override
        public E getId() {
            return e;
        }

        private static <E extends Identifiable> TreeIdRef<E> fromIdRef(IdRef<E> idRef) {
            //todo zrobic jakis sensowny falueOf w treeIfRefie - przepisuje niepotrzebnie
            //todo + zamiast ladowac do twinsow za kazdym razem robie nowy tree
            TreeIdRef<E> treeIdRef3 = new TreeIdRef<>();
            treeIdRef3.e = idRef.next.next.next.e;
            treeIdRef3.hashCode = idRef.next.next.next.hashCode;

            TreeIdRef<E> treeIdRef1 = new TreeIdRef<>();
            treeIdRef1.e = idRef.next.e;
            treeIdRef1.hashCode = idRef.next.hashCode;

            TreeIdRef<E> treeIdRef0 = new TreeIdRef<>();
            treeIdRef0.e = idRef.e;
            treeIdRef0.hashCode = idRef.hashCode;
            treeIdRef1.left = treeIdRef0;

            TreeIdRef<E> treeIdRef2 = new TreeIdRef<>();
            treeIdRef2.e = idRef.next.next.e;
            treeIdRef2.hashCode = idRef.next.next.hashCode;
            treeIdRef1.right = treeIdRef2;

            treeIdRef3.left = treeIdRef1;

            TreeIdRef<E> treeIdRef5 = new TreeIdRef<>();
            treeIdRef5.e = idRef.next.next.next.next.next.e;
            treeIdRef5.hashCode = idRef.next.next.next.next.next.hashCode;

            TreeIdRef<E> treeIdRef4 = new TreeIdRef<>();
            treeIdRef4.e = idRef.next.next.next.next.e;
            treeIdRef4.hashCode = idRef.next.next.next.next.hashCode;
            treeIdRef5.left = treeIdRef4;

            TreeIdRef<E> treeIdRef6 = new TreeIdRef<>();
            treeIdRef6.e = idRef.next.next.next.next.next.next.e;
            treeIdRef6.hashCode = idRef.next.next.next.next.next.next.hashCode;
            treeIdRef5.right = treeIdRef6;

            treeIdRef3.right = treeIdRef5;
            return treeIdRef3;
        }

    }
}
