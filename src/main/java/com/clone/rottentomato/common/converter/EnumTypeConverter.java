package com.clone.rottentomato.common.converter;

import com.clone.rottentomato.common.handler.EnumType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

@Converter(autoApply = true)
@RequiredArgsConstructor
@Slf4j
public class EnumTypeConverter<E extends Enum<E> & EnumType> implements AttributeConverter<E, String>{
    private final Class<E> enumType;


    @Override
    public String convertToDatabaseColumn(E attribute) {
        return !Objects.isNull(attribute) ? attribute.getKey() : null;
    }

    @Override
    public E convertToEntityAttribute(String value) {
        if(Objects.isNull(value)){
            return null;
        }
        // getKey 값을 기준으로 반환
        return Arrays.stream(enumType.getEnumConstants())
                .filter(t->t.getKey().equals(value))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Unknown enum key: "+ value));
    }
}
