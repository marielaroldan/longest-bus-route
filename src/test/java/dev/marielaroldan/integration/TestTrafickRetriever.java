package dev.marielaroldan.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dev.marielaroldan.TestUtil;
import dev.marielaroldan.domain.ImmutableJourneyPoint;
import dev.marielaroldan.domain.ImmutableStopsPoint;
import dev.marielaroldan.domain.JourneyPoint;
import dev.marielaroldan.domain.StopsPoint;
import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest
public class TestTrafickRetriever {

    @Test
    void successWhenCorrectResponseFromApi(WireMockRuntimeInfo wmRuntimeInfo) throws IOException, URISyntaxException {
        stubFor(get("/api2/linedata.json?key=123456&model=jour&DefaultTransportModeCode=BUS")
                .willReturn(ok(TestUtil.getAsStream("linedata_bus.json")).withGzipDisabled(true)));

        Either<String, List<JourneyPoint>> journeyPoints = new TraffickerRetriever(wmRuntimeInfo.getHttpBaseUrl()
                + "/api2/linedata.json", "123456").getJourneyPoints();

        List<JourneyPoint> journeyPoints1 = List.of(
                ImmutableJourneyPoint.builder().busLineNumber(1).direction(1).stopId(10008).build(),
                ImmutableJourneyPoint.builder().busLineNumber(1).direction(1).stopId(10012).build(),
                ImmutableJourneyPoint.builder().busLineNumber(1).direction(1).stopId(10014).build());

        Assertions.assertEquals(Either.right(journeyPoints1), journeyPoints);
    }

    @Test
    void successWhenCorrectResponseFromApiStop(WireMockRuntimeInfo wmRuntimeInfo) throws IOException, URISyntaxException {
        stubFor(get("/api2/linedata.json?key=123456&model=StopPoint&DefaultTransportModeCode=BUS")
                .willReturn(ok(TestUtil.getAsStream("linedata_bus_stopsnames.json")).withGzipDisabled(true)));

        Either<String, List<StopsPoint>> stopPoint = new TraffickerRetriever(wmRuntimeInfo.getHttpBaseUrl()
                + "/api2/linedata.json", "123456").getStops();

        List<StopsPoint> stopPoints1 = List.of(
                ImmutableStopsPoint.builder().stopNumber(10001).stopName("Stadshagsplan").build(),
                ImmutableStopsPoint.builder().stopNumber(10002).stopName("John Bergs plan").build(),
                ImmutableStopsPoint.builder().stopNumber(10003).stopName("John Bergs plan").build(),
                ImmutableStopsPoint.builder().stopNumber(10006).stopName("Arbetargatan").build());

        Assertions.assertEquals(Either.right(stopPoints1), stopPoint);
    }

    @Test
    void failFromRequestWith404(WireMockRuntimeInfo wmRuntimeInfo) {
        Either<String, List<JourneyPoint>> journeyPoints = new TraffickerRetriever(wmRuntimeInfo.getHttpBaseUrl()
                + "/api2/linedata.json", "123456").getJourneyPoints();

        Assertions.assertEquals(Either.left("Error when calling the service: 404"), journeyPoints);
    }

    @Test
    void test(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(get("/api2/linedata.json?key=123456&model=jour&DefaultTransportModeCode=BUS")
                .willReturn(ok("Not json")));
        Either<String, List<JourneyPoint>> journeyPoints = new TraffickerRetriever(wmRuntimeInfo.getHttpBaseUrl()
                + "/api2/linedata.json", "123456").getJourneyPoints();

        Assertions.assertEquals(Either.left("Error mapping to json Journey Point"), journeyPoints);
    }

    @Test
    void shouldFailBecauseTooManyRequestPerMont(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(get("/api2/linedata.json?key=123456&model=jour&DefaultTransportModeCode=BUS")
                .willReturn(ok("""
                                {
                                "StatusCode": 1007,
                                "Message": "Too many requests per month"
                                }
                        """).withGzipDisabled(true)));

        Either<String, List<JourneyPoint>> journeyPoints = new TraffickerRetriever(wmRuntimeInfo.getHttpBaseUrl()
                + "/api2/linedata.json", "123456").getJourneyPoints();

        Assertions.assertEquals(Either.left("Error: Too many requests per month 1007"), journeyPoints);
    }
}
