package com.graduationProject._thYear.Journal.models;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class JournalKindConverter implements AttributeConverter<JournalKind, String> {

    @Override
    public String convertToDatabaseColumn(JournalKind attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public JournalKind convertToEntityAttribute(String dbData) {
        return dbData != null ? JournalKind.valueOf(dbData) : null;
    }
}
