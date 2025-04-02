package com.clone.rottentomato.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** " , " 를 기준으로 구분하는 문자열 <-> 문자열 리스트로 변환하는 converter */
@Converter
public class CommaStringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SEPARATOR = ",";    // 쉼표(,) 기준으로 구분

    /** List<String> -> String (for db -> insert) */
    @Override
    public String convertToDatabaseColumn(List<String> values) {
        return CollectionUtils.isEmpty(values) ? StringUtils.EMPTY : StringUtils.join(values, SEPARATOR);
    }

    /** String -> List<String> (for java -> entity)*/
    @Override
    public List<String> convertToEntityAttribute(String dbStr) {
        return StringUtils.isBlank(dbStr) ? new ArrayList<>() : Arrays.asList(dbStr.split(SEPARATOR));
    }
}
