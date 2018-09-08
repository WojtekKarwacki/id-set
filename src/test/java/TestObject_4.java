import java.util.Arrays;
import java.util.Objects;

public class TestObject_4 implements Identifiable {
    private Integer id;

    private String[] array;

    TestObject_4(Integer id, String... array) {
        this.id = id;
        this.array = array;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject_4 that = (TestObject_4) o;
        return Objects.equals(id, that.id) &&
                Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }
}
