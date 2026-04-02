package com.vasileva.entity.converter;

import com.vasileva.entity.SpecialFeature;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class SpecialFeatureConverter implements AttributeConverter<Set<SpecialFeature>, String> {
    @Override
    public String convertToDatabaseColumn(Set<SpecialFeature> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream()
                .map(SpecialFeature::getCode)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<SpecialFeature> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return Collections.emptySet();
        return Arrays.stream(dbData.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(this::fromCode)
                .collect(Collectors.toSet());
    }

    private SpecialFeature fromCode(String code) {
        for (SpecialFeature f : SpecialFeature.values()) {
            if (f.getCode().equals(code)) return f;
        }
        throw new IllegalArgumentException("Invalid feature code: " + code);
    }
}
