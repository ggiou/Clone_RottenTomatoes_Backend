package com.clone.rottentomato.common.valid.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 특정 패키지(repository/custom/impl/)에서만 호출할 수 있도록 허용하는 커스텀 애너테이션
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AllowRepositoryCustomImpl {
}

