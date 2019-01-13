package idSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class IdWrapper_Test {

    @Test
    public void shouldReturnId() {
        IdWrapper<Integer, String> idWrapper = new IdWrapper<>(0, null);
        assertTrue(idWrapper.getId() == 0);
    }

    @Test
    public void shouldOverrideEqualsMethod() {
        IdWrapper<Integer, String> idWrapper0a = new IdWrapper<>(0, "0");
        IdWrapper<Integer, String> idWrapper0b = new IdWrapper<>(0, "0");
        IdWrapper<Integer, String> idWrapper1 = new IdWrapper<>(1, "1");
        assertTrue(idWrapper0a.equals(idWrapper0b) && idWrapper0b.equals(idWrapper0a));
        assertTrue(!idWrapper0a.equals(idWrapper1));
    }

    @Test
    public void shoulOverrideHashCodeMethod() {
        IdWrapper<Integer, String> idWrapper0a = new IdWrapper<>(0, "0");
        IdWrapper<Integer, String> idWrapper0b = new IdWrapper<>(0, "0");
        assertTrue(idWrapper0a.hashCode() == idWrapper0b.hashCode());
    }
}
