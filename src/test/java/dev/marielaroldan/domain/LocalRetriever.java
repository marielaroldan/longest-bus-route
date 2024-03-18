package dev.marielaroldan.domain;

import dev.marielaroldan.TestUtil;
import dev.marielaroldan.integration.stops.Stop;
import io.vavr.control.Either;
import io.vavr.control.Validation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class LocalRetriever implements BusAndStopRetriever {

    private final String busLineResponse;
    private final String stopsResponse;

    public LocalRetriever(String busLineResponse, String stopsResponse) {
        this.busLineResponse = busLineResponse;
        this.stopsResponse = stopsResponse;
    }

    @Override
    public Either<String, List<JourneyPoint>> getJourneyPoints() {
        try {
            List<dev.marielaroldan.integration.points.JourneyPoint> result = TestUtil.readJourneyPoint(busLineResponse)
                    .responseData().result();
            List<JourneyPoint> list = result.stream()
                    .map(journey -> JourneyPoint.validate(journey.lineNumber(), journey.directionCode(), journey.journeyPatternPointNumber()))
                    .map(Validation::get)
                    .toList();
            return Either.right(list);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Either<String, List<StopsPoint>> getStops() {
        try {
            List<Stop> result = TestUtil.readStopPoint(stopsResponse).responseData().result();
            List<StopsPoint> list = result.stream()
                    .map(stop -> StopsPoint.validate(stop.stopPointNumber(), stop.stopPointName()))
                    .map(Validation::get)
                    .toList();
            return Either.right(list);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
