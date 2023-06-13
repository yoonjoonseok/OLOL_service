package com.ll.olol.base.exception;

import com.ll.olol.base.rq.Rq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {
    private final Rq rq;

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        if (request.getRequestURI().equals("/recruitment/create") || request.getRequestURI().equals("/notification/list")) {
            return rq.historyBack("로그인을 선행해주세요");
        } else {
            // 다른 URL에 대한 처리 로직
            return "error-page";
        }
    }
}
