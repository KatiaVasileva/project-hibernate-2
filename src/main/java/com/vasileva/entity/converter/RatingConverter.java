package com.vasileva.entity.converter;

import com.vasileva.entity.Rating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {


    @Override
    public String convertToDatabaseColumn(Rating attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public Rating convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Rating.fromCode(dbData);
    }
}
