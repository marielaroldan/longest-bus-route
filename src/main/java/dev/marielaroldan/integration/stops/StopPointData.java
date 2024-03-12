package dev.marielaroldan.integration.stops;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StopPointData(@JsonProperty("Result") List<Stop> result) {
}
