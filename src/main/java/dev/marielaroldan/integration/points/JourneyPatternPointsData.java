package dev.marielaroldan.integration.points;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JourneyPatternPointsData(@JsonProperty("Result") List<JourneyPoint> result) {

}
