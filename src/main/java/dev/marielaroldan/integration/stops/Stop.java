package dev.marielaroldan.integration.stops;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Stop(@JsonProperty("StopPointNumber") String stopPointNumber,
                   @JsonProperty("StopPointName") String stopPointName,
                   @JsonProperty("StopAreaNumber") String stopAreaNumber,
                   @JsonProperty("LocationNorthingCoordinate") String locationNorthingCoordinate,
                   @JsonProperty("LocationEastingCoordinate") String locationEastingCoordinate,
                   @JsonProperty("ZoneShortName") String zoneShortName,
                   @JsonProperty("StopAreaTypeCode") String stopAreaTypeCode,
                   @JsonProperty("LastModifiedUtcDateTime") String lastModifiedUtcDateTime,
                   @JsonProperty("ExistsFromDate") String existsFromDate) {
}
