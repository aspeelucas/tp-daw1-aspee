package com.taller.tallermecanico.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ProblemDetail> manejarValidacion(
                        MethodArgumentNotValidException ex) {

                Map<String, String> errores = new LinkedHashMap<>();

                ex.getBindingResult().getFieldErrors().forEach(error -> errores.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage() != null
                                                ? error.getDefaultMessage()
                                                : "Valor inválido"));

                ProblemDetail problema = ProblemDetail.forStatus(
                                HttpStatus.BAD_REQUEST);

                problema.setTitle("Datos inválidos");
                problema.setDetail("La petición tiene errores");
                problema.setProperty("errores", errores);

                return ResponseEntity.badRequest().body(problema);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ProblemDetail> manejarJsonInvalido(
                        HttpMessageNotReadableException ex) {

                ProblemDetail problema = ProblemDetail.forStatus(
                                HttpStatus.BAD_REQUEST);

                problema.setTitle("JSON inválido");
                problema.setDetail(
                                "El cuerpo de la petición no tiene un formato JSON válido");

                return ResponseEntity.badRequest().body(problema);
        }

        @ExceptionHandler(RecursoNoEncontradoException.class)
        public ResponseEntity<ProblemDetail> manejarNoEncontrado(
                        RecursoNoEncontradoException ex) {

                ProblemDetail problema = ProblemDetail.forStatus(
                                HttpStatus.NOT_FOUND);

                problema.setTitle("Recurso no encontrado");
                problema.setDetail(ex.getMessage());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
        }

        @ExceptionHandler(ConflictoException.class)
        public ResponseEntity<ProblemDetail> manejarConflicto(
                        ConflictoException ex) {

                ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.CONFLICT);

                problema.setTitle("Conflicto");
                problema.setDetail(ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(problema);
        }

}
