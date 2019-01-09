package jmh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamsDto {

    @JsonProperty("function")
    private String function;
    @JsonProperty("numberOfElements")
    private String numberOfElements;
    @JsonProperty("testObjectType")
    private String testObjectType;

    public String getFunction() {
        return function;
    }

    public String getNumberOfElements() {
        return numberOfElements;
    }

    public String getTestObjectType() {
        return testObjectType;
    }
}
