package idSet;

import java.lang.reflect.Array;
import java.util.*;

public class FlexSet<E extends Identifiable> implements IdSet<E>, Identifiable {

    // package private access for test purposes
    static final int ID_REF_TREEIFY_THRESHOLD = 7;
    // package private access for test purposes
    static final int ID_REF_UNTREEIFY_THRESHOLD = 5;

    private static final int MAX_CAPACITY = Integer.MAX_VALUE >> 1;
    private static final int DEFAULT_INITIAL_CAPACITY = 16; // must be greater than or equal 16

    // package private access for test purposes
    IdRef<E>[] elements;
    private int size;

    // package private access for test purposes
    int capacity;
    // package private access for test purposes
    int modCapacity;
    // package private access for test purposes
    int expansionThreshold;
    // package private access for test purposes
    int shrinkThreshold;

    private FlexSet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Parameter initialCapacity should be greater than 0.");
        }
        size = 0;
        int highestOneBit = Integer.highestOneBit(initialCapacity);
        capacity = initialCapacity == highestOneBit ? initialCapacity : highestOneBit << 1;
        calculateModCapacity();
        calculateResizeThresholds();
        elements = initElements();
    }


    public static <T extends Identifiable> FlexSet<T> instance() {
        return instance(DEFAULT_INITIAL_CAPACITY);
    }

    public static <T extends Identifiable> FlexSet<T> instance(int initialCapacity) {
        return new FlexSet<>(initialCapacity);
    }

    @SafeVarargs
    public static <T extends Identifiable> FlexSet fromArray(T... a) {
        FlexSet<T> flexSet = instance(a.length);
        Collections.addAll(flexSet, a);
        return flexSet;
    }

    public static <K, V> FlexSet<IdWrapper<K, V>> fromMap(Map<K, V> map) {
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
        for (int i = 0; i < elements.length; i++) {
            elements[i] = new IdRef<>();
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
        return elements[modHashCode(hashCode)].get(id, hashCode);
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        return copyToArray(a);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        Objects.requireNonNull(a);
        ensureCapacityOfGivenArray(a);
        a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        return copyToArray(a);
    }

    @SuppressWarnings("unchecked")
    public <K> Map<K, E> toHashMap() {
        Map<K, E> map = new HashMap<>();
        for (E e : this) {
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
        int modHashCode = modHashCode(hashCode);
        IdRef<E> idRef = elements[modHashCode];
        if (idRef.add(e, hashCode)) {
            if (expandOnAdditionIfNeeded()) {
                treeifyIfNeeded(idRef, modHashCode, elements);
            }
            return true;
        }
        return false;
    }

    private boolean expandOnAdditionIfNeeded() {
        size++;
        if ((capacity < MAX_CAPACITY) && (size > expansionThreshold)) {
            capacity <<= 1;
            rebuild();
            return false;
        }
        return true;
    }

    private void treeifyIfNeeded(IdRef<E> idRef, int modHashCode, IdRef<E>[] elements) {
        if (idRef.size == ID_REF_TREEIFY_THRESHOLD && idRef.getClass() == IdRef.class) {
            elements[modHashCode] = TreeIdRef.fromIdRef(idRef);
        }
    }

    private void rebuild() {
        calculateModCapacity();
        calculateResizeThresholds();
        this.elements = rebuildElements();
    }

    private IdRef<E>[] rebuildElements() {
        IdRef<E>[] elements = initElements();
        for (IdRef<E> element : this.elements) {
            rebuildElement(elements, element);
        }
        return elements;
    }

    private void rebuildElement(IdRef<E>[] elements, IdRef<E> idRef) {
        while (idRef != null) {
            if (idRef.e != null) {
                doRebuildElement(elements, idRef);
            }
            idRef = idRef.next;
        }
    }

    private void doRebuildElement(IdRef<E>[] elements, IdRef<E> idRef) {
        int hashCode = idRef.hashCode;
        int modHashCode = modHashCode(hashCode);
        IdRef<E> idRefToRebuild = elements[modHashCode];
        idRefToRebuild.add(idRef.e, hashCode);
        treeifyIfNeeded(idRefToRebuild, modHashCode, elements);
    }


    @Override
    public boolean remove(Object o) {
        Objects.requireNonNull(o);
        return removeId(ensureTypeValid(o).getId()) != null;
    }

    @Override
    public E removeId(Object id) {
        int hashCode = id.hashCode();
        int modHashCode = modHashCode(hashCode);
        IdRef<E> idRef = elements[modHashCode];
        E e = idRef.removeId(id, hashCode);
        if (e != null) {
            if (shrinkOnRemovalIfNeeded()) {
                untreeifyIfNeeded(idRef, modHashCode, elements);
            }
        }
        return e;
    }

    private boolean shrinkOnRemovalIfNeeded() {
        size--;
        if ((capacity > 63) && (size < shrinkThreshold)) {
            capacity >>= 2;
            rebuild();
            return false;
        }
        return true;
    }

    private void untreeifyIfNeeded(IdRef<E> idRef, int modHashCode, IdRef<E>[] elements) {
        if (idRef.size == ID_REF_UNTREEIFY_THRESHOLD && idRef.getClass() == TreeIdRef.class) {
            elements[modHashCode] = TreeIdRef.toIdRef(idRef);
        }
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
                return current.next != null;
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
        for (E e : this) {
            idSet.add(e.getId());
        }
        return idSet;
    }

    @Override
    public Set<E> entrySet() {
        return new HashSet<>(this);
    }

    private int modHashCode(int hashCode) {
        return (hashCode ^ (hashCode >>> 16)) & modCapacity;
    }

    private void calculateModCapacity() {
        modCapacity = capacity - 1;
    }

    private void calculateResizeThresholds() {
        expansionThreshold = capacity;
        shrinkThreshold = capacity >> 2;

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

    // package private access for test purposes
    static class IdRef<E extends Identifiable> {

        int size;

        E e;

        IdRef<E> next;
        int hashCode;

        private IdRef() {
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
            IdRef<E> thisTemp = new IdRef<>();
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
            current.next = new IdRef<>();
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
            IdRef<E> idRef = new IdRef<>();
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
            IdRef<E> current = skipLowerHashCodes(hashCode);
            if (current == null) return null;
            while (current.e != null && current.hashCode == hashCode) {
                if (isFound(current, id)) {
                    return current.e;
                }
                current = current.next;
            }
            return null;
        }

        E removeId(Object id, int hashCode) {
            if (e == null) return null;
            if (e.getId().equals(id)) {
                return getRemovedAndAdjust(this);
            }
            return skipLowerHashCodesAndProceedWithRemoving(id, hashCode);
        }

        private E skipLowerHashCodesAndProceedWithRemoving(Object id, int hashCode) {
            IdRef<E> current = skipLowerHashCodes(hashCode);
            if (current == null) return null;
            while (current.e != null && current.hashCode == hashCode) {
                if (isFound(current, id)) {
                    return getRemovedAndAdjust(current);
                }
                current = current.next;
            }
            return null;
        }

        private void adjustOnRemoval(IdRef<E> current, IdRef<E> next) {
            current.e = next.e;
            current.next = next.next;
            current.hashCode = next.hashCode;
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
            size--;
            return e;
        }

        private boolean isFound(IdRef<E> current, Object id) {
            return current.e.getId().equals(id);
        }

        @Override
        public String toString() {
            return "IdRef{" +
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

    // package private access for test purposes
    static final class TreeIdRef<E extends Identifiable> extends IdRef<E> {

        private TreeIdRef<E> left;
        private TreeIdRef<E> right;

        private IdRef<E> block = new IdRef<>();


        private int nodesCount;

        private TreeIdRef() {
        }

        @Override
        boolean add(E e, int hashCode) {
            if (addByHashCode(this, null, e, hashCode, 0)) {
                size++;
                return true;
            }
            return false;
        }

        private boolean addByHashCode(TreeIdRef<E> current, TreeIdRef<E> parent, E e, int hashCode, int consecutiveNodesCount) {
            if (hashCode == current.hashCode) {
                return handleEqualHashCodes(current, parent, e, hashCode, consecutiveNodesCount);
            } else if (hashCode < current.hashCode) {
                return checkLeft(current, e, hashCode, consecutiveNodesCount) || addByHashCode(current.left, current, e, hashCode, consecutiveNodesCount + 1);
            } else {
                return checkRight(current, e, hashCode, consecutiveNodesCount) || addByHashCode(current.right, current, e, hashCode, consecutiveNodesCount + 1);
            }
        }

        private boolean handleEqualHashCodes(TreeIdRef<E> current, TreeIdRef<E> parent, E e, int hashCode, int consecutiveNodesCount) {
            return !checkFirst(current, e) && (handleNull(current, parent, e, consecutiveNodesCount) || handleBlock(current, e, hashCode));
        }

        private boolean checkFirst(TreeIdRef<E> current, E e) {
            return e.equals(current.e);
        }

        private boolean handleNull(TreeIdRef<E> current, TreeIdRef<E> parent, E e, int consecutiveNodesCount) {
            if (current.e == null) {
                current.e = e;
                parent.next = current;
                rebuildTreeIdRefIfNeeded(consecutiveNodesCount);
                return true;
            }
            return false;
        }

        private boolean handleBlock(TreeIdRef<E> current, E e, int hashCode) {
            current.next = current.block;
            return current.block.add(e, hashCode);
        }

        private boolean checkLeft(TreeIdRef<E> current, E e, int hashCode, int consecutiveNodesCount) {
            if (current.left == null) {
                current.left = createLinkedTreeIdRef(current);
                current.left.e = e;
                current.left.hashCode = hashCode;
                rebuildTreeIdRefIfNeeded(consecutiveNodesCount);
                return true;
            }
            return false;
        }

        private boolean checkRight(TreeIdRef<E> current, E e, int hashCode, int consecutiveNodesCount) {
            if (current.right == null) {
                current.right = createLinkedTreeIdRef(current);
                current.right.e = e;
                current.right.hashCode = hashCode;
                rebuildTreeIdRefIfNeeded(consecutiveNodesCount);
                return true;
            }
            return false;
        }

        private TreeIdRef<E> createLinkedTreeIdRef(TreeIdRef<E> parent) {
            TreeIdRef<E> treeIdRef = new TreeIdRef<>();
            IdRef<E> parentNext = parent.next;
            parent.next = treeIdRef;
            treeIdRef.next = parentNext;
            return treeIdRef;
        }

        @Override
        E get(Object id, int hashCode) {
            TreeIdRef<E> treeIdRef = findByHashCode(this, hashCode);
            if (treeIdRef == null) {
                return null;
            }
            E e = treeIdRef.e;
            if (e == null || e.getId().equals(id)) {
                return e;
            }
            return treeIdRef.block.get(id, hashCode);
        }

        private TreeIdRef<E> findByHashCode(TreeIdRef<E> current, int hashCode) {
            if (hashCode == current.hashCode) {
                return current;
            } else {
                if (hashCode < current.hashCode) {
                    current = current.left;
                } else {
                    current = current.right;
                }
                if (current == null) {
                    return null;
                }
            }
            return findByHashCode(current, hashCode);
        }

        @Override
        E removeId(Object id, int hashCode) {
            TreeIdRef<E> treeIdRef = findByHashCode(this, hashCode);
            if (treeIdRef == null) {
                return null;
            }
            E e = treeIdRef.e;
            if (e == null) {
                return null;
            }
            IdRef<E> block = treeIdRef.block;
            if (e.getId().equals(id)) {
                if (block.size == 0) {
                    treeIdRef.e = null;
                } else {
                    treeIdRef.e = block.removeId(block.e.getId(), hashCode);
                }
                size--;
                return e;
            }
            return null;
        }


        private void rebuildTreeIdRefIfNeeded(int consecutiveNodesCount) {
            nodesCount++;
            if ((28 - Integer.numberOfLeadingZeros(nodesCount) + ID_REF_TREEIFY_THRESHOLD < consecutiveNodesCount)) {
                rebuildTreeIdRef();
            }
        }

        private void rebuildTreeIdRef() {
            TreeIdRef<E>[] temporarySortedTreeIdRefs = createTemporarySortedTreeIdRefs();
            TreeIdRef<E> rebuilt = rebuildFromSorted(temporarySortedTreeIdRefs);
            e = rebuilt.e;
            hashCode = rebuilt.hashCode;
            block = rebuilt.block;
            next = rebuilt.next;
            left = rebuilt.left;
            right = rebuilt.right;
        }

        @SuppressWarnings("unchecked")
        private TreeIdRef<E>[] createTemporarySortedTreeIdRefs() {
            TreeIdRef<E>[] temporarySortedTreeIdRefs = new TreeIdRef[nodesCount];
            sortStartingFromLeftmost(temporarySortedTreeIdRefs, this.left, this, 0);
            return temporarySortedTreeIdRefs;
        }

        private int sortStartingFromLeftmost(TreeIdRef<E>[] temporarySortedTreeIdRefs, TreeIdRef<E> current, TreeIdRef<E> parent, int i) {
            i = sortForCurrent(temporarySortedTreeIdRefs, current, i);
            i = sortForParent(temporarySortedTreeIdRefs, current, parent, i);
            return i;
        }

        private int sortForParent(TreeIdRef<E>[] temporarySortedTreeIdRefs, TreeIdRef<E> current, TreeIdRef<E> parent, int i) {
            if (parent.right != current) {
                if (parent.e != null) {
                    temporarySortedTreeIdRefs[i++] = parent;
                }
                if (parent.right != null) {
                    i = sortForCurrent(temporarySortedTreeIdRefs, parent.right, i);
                }
            }
            return i;
        }

        private int sortForCurrent(TreeIdRef<E>[] temporarySortedTreeIdRefs, TreeIdRef<E> current, int i) {
            if (current.left != null) {
                i = sortStartingFromLeftmost(temporarySortedTreeIdRefs, current.left, current, i);
            } else {
                if (current.e != null) {
                    temporarySortedTreeIdRefs[i++] = current;
                }
                if (current.right != null) {
                    i = sortStartingFromLeftmost(temporarySortedTreeIdRefs, current.right, current, i);
                }
            }
            return i;
        }

        private TreeIdRef<E> rebuildFromSorted(TreeIdRef<E>[] temporarySortedTreeIdRefs) {
            int index = temporarySortedTreeIdRefs.length >> 1;
            TreeIdRef<E> midTreeIdRef = temporarySortedTreeIdRefs[index].createStandaloneCopy();
            FlexSet<IntegerIdentifiable> usedIndices = new FlexSet<>(nodesCount << 1);
            usedIndices.add(new IntegerIdentifiable(index));
            midTreeIdRef.nodesCount = rebuildForBoth(temporarySortedTreeIdRefs, midTreeIdRef, 0, index, temporarySortedTreeIdRefs.length, usedIndices);
            return midTreeIdRef;
        }

        private int rebuildForBoth(TreeIdRef<E>[] temporarySortedTreeIdRefs, TreeIdRef<E> midTreeIdRef, int from, int index, int to, FlexSet<IntegerIdentifiable> usedIndices) {
            int nodesCount = rebuildForLeft(temporarySortedTreeIdRefs, midTreeIdRef, from, index, usedIndices);
            nodesCount += rebuildForRight(temporarySortedTreeIdRefs, midTreeIdRef, index, to, usedIndices);
            return nodesCount;
        }

        private int rebuildForLeft(TreeIdRef<E>[] temporarySortedTreeIdRefs, TreeIdRef<E> midTreeIdRef, int from, int to, FlexSet<IntegerIdentifiable> usedIndices) {
            midTreeIdRef.next = null;
            int diff = to - from;
            if (diff != 0) {
                int index = (diff >> 1) + from;
                if (usedIndices.add(new IntegerIdentifiable(index))) {
                    TreeIdRef<E> treeIdRef = temporarySortedTreeIdRefs[index].createStandaloneCopy();
                    midTreeIdRef.left = treeIdRef;
                    midTreeIdRef.next = treeIdRef;
                    return rebuildForBoth(temporarySortedTreeIdRefs, midTreeIdRef.left, from, index, to, usedIndices);
                }
            }
            midTreeIdRef.left = null;
            return 0;
        }

        private int rebuildForRight(TreeIdRef<E>[] temporarySortedTreeIdRefs, TreeIdRef<E> midTreeIdRef, int from, int to, FlexSet<IntegerIdentifiable> usedIndices) {
            int diff = to - from;
            if (diff != 1) {
                int index = (diff >> 1) + from;
                if (usedIndices.add(new IntegerIdentifiable(index))) {
                    TreeIdRef<E> treeIdRef = temporarySortedTreeIdRefs[index].createStandaloneCopy();
                    midTreeIdRef.right = treeIdRef;
                    getLast(midTreeIdRef).next = treeIdRef;
                    return rebuildForBoth(temporarySortedTreeIdRefs, midTreeIdRef.right, from, index, to, usedIndices);
                }
            }
            midTreeIdRef.right = null;
            return 0;
        }

        private IdRef<E> getLast(IdRef<E> midTreeIdRef) {
            while (midTreeIdRef.next != null) {
                midTreeIdRef = midTreeIdRef.next;
            }
            return midTreeIdRef;
        }

        private TreeIdRef<E> createStandaloneCopy() {
            TreeIdRef<E> treeIdRef = new TreeIdRef<>();
            treeIdRef.e = e;
            treeIdRef.hashCode = hashCode;
            treeIdRef.block = block;
            return treeIdRef;
        }

        private static <E extends Identifiable> TreeIdRef<E> fromIdRef(IdRef<E> idRef) {
            TreeIdRef<E> treeIdRef = new TreeIdRef<>();
            treeIdRef.e = idRef.next.next.next.e;
            treeIdRef.hashCode = idRef.next.next.next.hashCode;
            treeIdRef.size = 1;
            treeIdRef.nodesCount = 1;
            fromIdRefHelp(treeIdRef, idRef.next);
            fromIdRefHelp(treeIdRef, idRef.next.next.next.next.next);
            fromIdRefHelp(treeIdRef, idRef);
            fromIdRefHelp(treeIdRef, idRef.next.next);
            fromIdRefHelp(treeIdRef, idRef.next.next.next.next);
            fromIdRefHelp(treeIdRef, idRef.next.next.next.next.next.next);
            return treeIdRef;
        }

        private static <E extends Identifiable> void fromIdRefHelp(TreeIdRef<E> treeIdRef, IdRef<E> idRef) {
            treeIdRef.add(idRef.e, idRef.hashCode);
        }

        private static <E extends Identifiable> IdRef<E> toIdRef(IdRef<E> treeIdRef) {
            IdRef<E> idRef = new IdRef<>();
            while (treeIdRef != null) {
                if (treeIdRef.e != null) {
                    idRef.add(treeIdRef.e, treeIdRef.hashCode);
                }
                treeIdRef = treeIdRef.next;
            }
            return idRef;
        }

    }

    private static class IntegerIdentifiable implements Identifiable {

        private final int integer;

        private IntegerIdentifiable(int integer) {
            this.integer = integer;
        }

        @Override
        public Object getId() {
            return integer;
        }

        @Override
        public boolean equals(Object o) {
            return o != null && integer == ((IntegerIdentifiable) o).integer;
        }

        @Override
        public int hashCode() {
            return integer;
        }
    }


}
