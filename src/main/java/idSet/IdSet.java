package idSet;

import java.util.Collection;
import java.util.Set;

public interface IdSet<E extends Identifiable> extends Set<E> {

    boolean containsId(Object o);

    E getByElem(E e);

    E get(Object id);

    E removeId(Object o);

    boolean containsAllIds(Collection<?> c);

    boolean removeAllIds(Collection<?> c);

    boolean retainAllIds(Collection<?> c);

    Set<Object> idSet();

    Set<E> entrySet();



}
