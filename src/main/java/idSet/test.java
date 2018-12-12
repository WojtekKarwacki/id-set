package idSet;

import com.atlassian.httpclient.api.factory.HttpClientOptions;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class test {

    public static void main(String[] args) {
        JiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();
        HttpClientOptions httpClientOptions = new HttpClientOptions();
        httpClientOptions.
    }
}
