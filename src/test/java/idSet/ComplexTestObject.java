package idSet;

import java.util.Objects;

public class ComplexTestObject implements Identifiable {
    public static class IntegerId {

        private Integer id;

        public IntegerId(Integer id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntegerId integerId = (IntegerId) o;
            return Objects.equals(id, integerId.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

    }
    private IntegerId id;

    public ComplexTestObject(IntegerId id) {
        this.id = id;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexTestObject that = (ComplexTestObject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
