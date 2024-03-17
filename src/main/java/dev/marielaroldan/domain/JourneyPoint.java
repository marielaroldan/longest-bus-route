package dev.marielaroldan.domain;

import dev.marielaroldan.domain.validation.ValidationUtil;
import io.vavr.Tuple;
import io.vavr.collection.Traversable;
import io.vavr.control.Validation;
import org.immutables.value.Value;

@Value.Immutable
public interface JourneyPoint {
    int busLineNumber();

    int direction();

    long stopId();

    static Validation<String, JourneyPoint> validate(final String lineNumber,
                                                     final String direction,
                                                     final String stopId) {
        return Validation.combine(
                        ValidationUtil.validateInteger(lineNumber),
                        ValidationUtil.validateInteger(direction),
                        ValidationUtil.validateLong(stopId)
                ).ap(Tuple::of)
                .map(tuple -> (JourneyPoint) ImmutableJourneyPoint.builder()
                        .busLineNumber(tuple._1)
                        .direction(tuple._2)
                        .stopId(tuple._3).build()
                ).mapError(Traversable::mkString);
    }
}
