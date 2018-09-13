package idSet;

import java.util.Objects;

public class TestObject_3 implements Identifiable {

    private TestId id;

    TestObject_3(int id) {
        this.id = new TestId(id);
    }

    @Override
    public TestId getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject_3 that = (TestObject_3) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    static class TestId {
        private int number;
        TestId(int number) {
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestId testId = (TestId) o;
            return number == testId.number;
        }

        @Override
        public int hashCode() {
            return Objects.hash(number);
        }
    }
}
