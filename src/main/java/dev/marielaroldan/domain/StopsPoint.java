package dev.marielaroldan.domain;

import dev.marielaroldan.domain.validation.ValidationUtil;
import io.vavr.Tuple;
import io.vavr.collection.Traversable;
import io.vavr.control.Validation;
import org.immutables.value.Value;

@Value.Immutable
public interface StopsPoint {
    long stopNumber();

    String stopName();

    static Validation<String, StopsPoint> validate(final String stopNumber,
                                                   final String stopName) {
        return Validation.combine(
                        ValidationUtil.validateString(stopName),
                        ValidationUtil.validateLong(stopNumber)
                ).ap(Tuple::of)
                .map(tuple -> (StopsPoint) ImmutableStopsPoint.builder()
                        .stopName(tuple._1)
                        .stopNumber(tuple._2)
                        .build()
                ).mapError(Traversable::mkString);
    }
}
