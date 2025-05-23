package com.example.bankcards.entity.converter;

import jakarta.persistence.AttributeConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;

public class YearMonthDateAttributeConverter implements AttributeConverter<YearMonth, Date> {
    @Override
    public Date convertToDatabaseColumn(YearMonth yearMonth) {
        if(yearMonth != null){
            return Date.valueOf(yearMonth.atDay(1));
        }
        return null;
    }

    @Override
    public YearMonth convertToEntityAttribute(Date dbData) {
        if (dbData != null){
            LocalDate localDate = dbData.toLocalDate();
            return YearMonth.of(localDate.getYear(), localDate.getMonth());
        }
        return null;
    }
}
