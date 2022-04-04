package com.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

        Logger logger = LoggerFactory.getLogger(LogAspect.class);

        // 공통적인 작업을 구현하기 위한 용도
        // 패키지가 com.example.controller인 컨트롤러는 모두 수행
        @Around("execution(* com.example.controller.*Controller.*(..)) or execution(* com.example.mapper.*Mapper.*(..))")
        public Object printLog(ProceedingJoinPoint joinPoint)
                        throws Throwable {
                // 현재 시간 보관
                long start = System.currentTimeMillis();

                // 수행되는 클래스명
                String className = joinPoint.getSignature().getDeclaringTypeName();

                // 어느 controller에서 어떤 mapper가 출력되는지
                String type = "";
                if (className.contains("Controller") == true) {
                        type = "Controller => ";
                } else if (className.contains("Service") == true) {
                        type = "Service => ";
                } else if (className.contains("Mapper") == true) {
                        type = "Mapper => ";
                }

                // 끝나는 시간
                // 끝나는 시간 > 현재시간
                // 끝나는시간 - 현재시간 = 수행시간
                long end = System.currentTimeMillis();
                String methodName = joinPoint.getSignature().getName();
                logger.info(type + className + "->" + methodName);
                logger.info("execute time : " + (end - start));
                // System.out.println("클래스명 : " + className + ", 메소드명 : " + methodName);
                return joinPoint.proceed();
        }

}
