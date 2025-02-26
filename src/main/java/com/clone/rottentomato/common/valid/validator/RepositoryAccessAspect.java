package com.clone.rottentomato.common.valid.validator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryAccessAspect {

    /** custom repository impl 에서만 사용가능하도록 제한한 벨리데이터*/
    @Around("@annotation(com.clone.rottentomato.common.valid.validation.AllowRepositoryCustomImpl)")
    public Object restrictAccessToRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        // 호출한 클래스가 repository/custom/impl/ 패키지인지 확인
        String callingClass = joinPoint.getTarget().getClass().getPackage().getName();

        // 패키지가 "repository.custom.impl"로 시작하는지 체크

        if (!callingClass.matches("com\\.clone\\.rottentomato\\..*\\..*\\.repository\\.custom\\.impl")){
            throw new IllegalAccessException("이 메서드는 repository/custom/impl/ 패키지에서만 호출할 수 있습니다.");
        }

        // 메서드 실행
        return joinPoint.proceed();
    }
}
