package idSet;

import com.atlassian.jira.rest.client.api.JiraRestClientFactory;

import java.util.Objects;

public class TestObject_0 implements Identifiable {
    private Integer id;

    TestObject_0(Integer id) {
        this.id = id;
        JiraRestClientFactory
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject_0 that = (TestObject_0) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
