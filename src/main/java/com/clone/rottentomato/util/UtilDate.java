package com.clone.rottentomato.util;

import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.exception.CommonException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static com.clone.rottentomato.common.constant.CommonConst.DATE.*;

public class UtilDate {
    final static DateTimeFormatter format_defaultDate = DateTimeFormatter.ofPattern(DEFAULT_DATE);
    final static DateTimeFormatter format_defaultDateTime = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME);

    public static String getNowDate(){
        return getLocalDateTime(LocalDateTime.now(), DEFAULT_DATE);
    }
    public static String getNowDateTime(){
        return getLocalDateTime(LocalDateTime.now(), DEFAULT_DATE_TIME);
    }

    /** LocalDateTime 지정한 format의 문자열로 변경하는 함수 */
    public static String getLocalDateTime(LocalDateTime localDateTime, String format) {
        if(StringUtils.isBlank(format)) format = DEFAULT_DATE_TIME;
        if(Objects.isNull(localDateTime)) localDateTime = LocalDateTime.now();
        return DateTimeFormatter.ofPattern(format).format(localDateTime);
    }

    /** 문자열 날짜의 형식이 유효한지 확인하는 함수 */
    public static boolean isValidDate(String dateStr){
        if(StringUtils.isBlank(dateStr)) return false;
        return isValidParse(dateStr, format_defaultDate)
                || isValidParse(dateStr, format_defaultDateTime);
    }

    /** 문자열 날짜에 맞는 formatter을 반환해주는 함수 */
    public static DateTimeFormatter getParsingType(String dateStr){
        if (isValidParse(dateStr, format_defaultDate)){
            return format_defaultDate;
        }
        if (isValidParse(dateStr, format_defaultDateTime)){
            return format_defaultDateTime;
        }
        return null;
    }

    /** 주어진 포맷으로 날짜를 파싱 시도하는 메서드 */
    public static boolean isValidParse(String dateStr, DateTimeFormatter formatter) {
        try {
            formatter.parse(dateStr);  // 문자열을 날짜로 파싱
            return true;  // 파싱 성공
        } catch (DateTimeParseException e) {
            return false;  // 파싱 실패
        }
    }

    /** 날짜 문자열이 유효하다면 날짜로 변환, 없다면 throw */
    public static LocalDateTime getLocalDateTime(String dateStr){
        DateTimeFormatter parser = getParsingType(dateStr);
        if(Objects.isNull(parser)) throw new CommonException("유요한 날짜 문자열이 아닙니다.", CommonError.DATE_FORMAL_ERROR);
        if(parser.equals(format_defaultDate) || parser.equals(format_defaultDateTime)) return LocalDate.parse(dateStr, parser).atStartOfDay();
        return LocalDateTime.parse(dateStr, parser);
    }

    /** 날짜 문자열이 유효하다면 날짜로 변환, 없다면 now 반환 */
    public static LocalDateTime getLocalDateTimeOrNow(String dateStr){
        DateTimeFormatter parser = getParsingType(dateStr);
        if(Objects.isNull(parser)) return LocalDateTime.now();
        if(parser.equals(format_defaultDate) || parser.equals(format_defaultDateTime)) return LocalDate.parse(dateStr, parser).atStartOfDay();
        return LocalDateTime.parse(dateStr, parser);
    }


    /** 날짜 문자열이 유효하다면 날짜로 변환, 없다면 설정한 default 값 반환 */
    public static LocalDateTime getLocalDateTimeOrEls(String dateStr, LocalDateTime defaultDateTime){
        DateTimeFormatter parser = getParsingType(dateStr);
        if(Objects.isNull(parser)) return defaultDateTime;
        if(parser.equals(format_defaultDate) || parser.equals(format_defaultDateTime)) return LocalDate.parse(dateStr, parser).atStartOfDay();
        return LocalDateTime.parse(dateStr, parser);
    }

    /** LocalDateTime 을 기본 날짜 포맷의 문자열로 변환하는 함수 
     *  - isNullable = true 라면, 빈문자열로 반환
     * */
    public static String convertDate(LocalDateTime dateTime, boolean isNullable){
        if(isNullable) return StringUtils.EMPTY;
        if(Objects.isNull(dateTime)) return StringUtils.EMPTY;  // 에러 코드로 변환
        return dateTime.format(DEFAULT_DATE_FORMATTER);
    }

    /** LocalDateTime 을 기본 날짜 포맷의 문자열로 변환하는 함수 */
    public static String convertDate(LocalDateTime dateTime){
        return convertDate(dateTime, false);
    }
}
