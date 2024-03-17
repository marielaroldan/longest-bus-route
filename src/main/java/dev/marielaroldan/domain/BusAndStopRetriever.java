package dev.marielaroldan.domain;

import io.vavr.control.Either;

import java.util.List;

public interface BusAndStopRetriever {

    Either<String, List<JourneyPoint>> getJourneyPoints();

    Either<String, List<StopsPoint>> getStops();

}
