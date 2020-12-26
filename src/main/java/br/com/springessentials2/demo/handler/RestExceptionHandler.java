package br.com.springessentials2.demo.handler;

import br.com.springessentials2.demo.exception.BadRequestException;
import br.com.springessentials2.demo.exception.BadRequestExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice //todos os meus controllers tem de usar essa classe para tratamento de exceptions;
public class RestExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre) {

        return new ResponseEntity<>(BadRequestExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Bad Request Exception")
                .details(bre.getMessage())
                .developerMessage(getClass().getName())
                .build(), HttpStatus.BAD_REQUEST);

    }
}
