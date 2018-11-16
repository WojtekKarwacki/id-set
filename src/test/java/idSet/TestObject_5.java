package idSet;

import java.util.Objects;

public class TestObject_5 implements Identifiable {
    int id;

    public TestObject_5(int id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
