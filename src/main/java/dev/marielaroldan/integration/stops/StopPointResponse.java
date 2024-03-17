package dev.marielaroldan.integration.stops;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StopPointResponse(@JsonProperty("ResponseData") StopPointData responseData,
                                @JsonProperty("Message") String message,
                                @JsonProperty("StatusCode") String statusCode) {
}
