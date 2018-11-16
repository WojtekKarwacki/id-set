package idSet;

public class TestObject_6 implements Identifiable {
    public static class Id {
        int id;

        public Id(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id1 = (Id) o;
            return id == id1.id;
        }

        @Override
        public int hashCode() {
            return 0;
        }

    }
    private Id id;

    public TestObject_6(int id) {
        this.id = new Id(id);
    }

    @Override
    public Id getId() {
        return id;
    }
}
