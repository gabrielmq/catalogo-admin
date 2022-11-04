package com.fullcycle.catalogo.admin.infrastructure.video.persistence.converter;

import com.fullcycle.catalogo.admin.domain.video.rating.Rating;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {
    @Override
    public String convertToDatabaseColumn(final Rating attribute) {
        return Optional.ofNullable(attribute)
                .map(Rating::getName).orElse(null);
    }

    @Override
    public Rating convertToEntityAttribute(final String dbData) {
        return Optional.ofNullable(dbData)
                .flatMap(Rating::of).orElse(null);
    }
}
