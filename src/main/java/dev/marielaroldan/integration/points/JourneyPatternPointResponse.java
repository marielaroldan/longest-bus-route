package dev.marielaroldan.integration.points;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JourneyPatternPointResponse(@JsonProperty("ResponseData") JourneyPatternPointsData responseData,
                                          @JsonProperty("Message") String message,
                                          @JsonProperty("StatusCode") String statusCode) {
}
