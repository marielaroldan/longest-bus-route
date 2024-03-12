package dev.marielaroldan.integration.points;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JourneyPoint(@JsonProperty("LineNumber") String lineNumber,
                           @JsonProperty("DirectionCode") String directionCode,
                           @JsonProperty("JourneyPatternPointNumber") String journeyPatternPointNumber,
                           @JsonProperty("LastModifiedUtcDateTime") String lastModifiedUtcDateTime,
                           @JsonProperty("ExistsFromDate") String existsFromDate) {

}
