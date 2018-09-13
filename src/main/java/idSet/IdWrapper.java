package idSet;

import java.util.Objects;

//todo test do equals i hashcode oraz test do getid
public class IdWrapper<K, V> implements Identifiable {

    private K id;
    private V value;

    IdWrapper(K id, V value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public K getId() {
        return id;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdWrapper<?, ?> idWrapper = (IdWrapper<?, ?>) o;
        return Objects.equals(id, idWrapper.id) &&
                Objects.equals(value, idWrapper.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}