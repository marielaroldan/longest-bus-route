package dev.marielaroldan.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marielaroldan.domain.BusAndStopRetriever;
import dev.marielaroldan.domain.JourneyPoint;
import dev.marielaroldan.domain.StopsPoint;
import dev.marielaroldan.integration.points.JourneyPatternPointResponse;
import dev.marielaroldan.integration.stops.StopPointResponse;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class TraffickerRetriever implements BusAndStopRetriever {

    private final HttpClient client;
    final private String uri;
    final private String key;

    public TraffickerRetriever(final String uri, final String key) {
        this.client = HttpClient.newHttpClient();
        this.uri = uri;
        this.key = key;
    }

    @Override
    public Either<String, List<JourneyPoint>> getJourneyPoints() {
        return getUri("jour")
                .fold(
                        error -> Either.left("Error creating uri"),
                        uri -> getJourneyPoints(uri)
                                .map(this::mapToJourneyPoints)
                );
    }

    private List<JourneyPoint> mapToJourneyPoints(JourneyPatternPointResponse response) {
        return response.responseData().result().stream()
                .map(journeyPoint -> JourneyPoint.validate(journeyPoint.lineNumber(),
                        journeyPoint.directionCode(),
                        journeyPoint.journeyPatternPointNumber())
                )
                .filter(Validation::isValid)
                .map(Validation::get)
                .toList();
    }

    private Either<String, JourneyPatternPointResponse> getJourneyPoints(URI uri) {
        try {
            var response = client.send(getRequest(uri), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return Either.left("Error when calling the service: " + response.statusCode());
            }
            var body = response.body();
            return getJourneyPatternPointResponse(body);
        } catch (IOException | InterruptedException e) {
            return Either.left("Error: cannot communicate with the service" + e.getMessage());
        }
    }

    private static HttpRequest getRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMillis(5000))
                .header("Content-Type", "application/json")
                .header("Accept-Encoding", "gzip, deflate")
                .build();
    }

    private Either<String, JourneyPatternPointResponse> getJourneyPatternPointResponse(String response) {
        try {
            var journey = new ObjectMapper().readValue(response, JourneyPatternPointResponse.class);
            if (journey.responseData() == null) {
                return Either.left("Error: " + journey.message() + " " + journey.statusCode());
            }
            return Either.right(journey);
        } catch (JsonProcessingException e) {
            return Either.left("Error mapping to json Journey Point");
        }
    }

    @Override
    public Either<String, List<StopsPoint>> getStops() {
        return getUri("StopPoint")
                .fold(
                        error -> Either.left("Error creating uri"),
                        uri -> getStopsPointResponse(uri)
                                .map(this::mapToStopsPoint)
                );
    }

    private List<StopsPoint> mapToStopsPoint(StopPointResponse response) {
        return response
                .responseData()
                .result()
                .stream()
                .map(stop -> StopsPoint.validate(stop.stopPointNumber(),
                        stop.stopPointName()))
                .filter(Validation::isValid)
                .map(Validation::get)
                .toList();
    }

    private Either<String, StopPointResponse> getStopsPointResponse(URI uri) {
        try {
            HttpResponse<String> response = client.send(getRequest(uri), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return Either.left("Error when calling the service: " + response.statusCode());
            }
            var body = response.body();
            return readStopPoint(body);
        } catch (IOException | InterruptedException e) {
            return Either.left("Error: cannot communicate with the service" + e.getMessage());
        }
    }

    private Either<String, URI> getUri(final String modelApi) {
        try {
            return Either.right(new URIBuilder(uri)
                    .addParameter("key", key)
                    .addParameter("model", modelApi)
                    .addParameter("DefaultTransportModeCode", "BUS")
                    .build());
        } catch (URISyntaxException e) {
            return Either.left("error creating uri");
        }
    }

    private Either<String, StopPointResponse> readStopPoint(String response) {
        try {
            var stopsResponse = new ObjectMapper().readValue(response, StopPointResponse.class);
            if (stopsResponse.responseData() == null) {
                return Either.left("Error: " + stopsResponse.message() + " " + stopsResponse.statusCode());
            }
            return Either.right(stopsResponse);
        } catch (JsonProcessingException e) {
            return Either.left("Error mapping Stop Points");
        }
    }
}
