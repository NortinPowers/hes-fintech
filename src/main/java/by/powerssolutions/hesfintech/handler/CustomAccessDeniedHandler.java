package by.powerssolutions.hesfintech.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.powerssolutions.hesfintech.model.BaseResponse;
import by.powerssolutions.hesfintech.model.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final CustomExceptionHandler customExceptionHandler;

    /**
     * Обрабатывает исключение доступа, вызванное отказом в доступе.
     *
     * @param request               HTTP-запрос.
     * @param response              HTTP-ответ.
     * @param accessDeniedException Исключение доступа.
     * @throws IOException Если возникла ошибка ввода-вывода при записи в ответ.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ResponseEntity<BaseResponse> responseEntity = customExceptionHandler.handleException(accessDeniedException);
        ExceptionResponse exceptionResponse = (ExceptionResponse) responseEntity.getBody();
        response.setContentType(APPLICATION_JSON_VALUE);
        if (exceptionResponse != null) {
            response.setStatus(exceptionResponse.getStatus());
        }
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(exceptionResponse));
    }
}
