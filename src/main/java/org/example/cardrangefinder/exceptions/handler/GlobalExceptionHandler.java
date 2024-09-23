package org.example.cardrangefinder.exceptions.handler;

import org.example.cardrangefinder.dto.ResponseMessageDto;
import org.example.cardrangefinder.exceptions.DatabaseNotReadyException;
import org.example.cardrangefinder.exceptions.FileDownloadException;
import org.example.cardrangefinder.exceptions.MoreThanOneRangeException;
import org.example.cardrangefinder.exceptions.DatabaseOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DatabaseNotReadyException.class)
    public ResponseEntity<ResponseMessageDto> handleDatabaseNotReady(DatabaseNotReadyException ex) {
        ResponseMessageDto responseMessage = new ResponseMessageDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessageDto> handleIllegalArgument(IllegalArgumentException ex) {
        ResponseMessageDto responseMessage = new ResponseMessageDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
    }

    @ExceptionHandler(FileDownloadException.class)
    public ResponseEntity<ResponseMessageDto> handleFileDownload(FileDownloadException ex) {
        ResponseMessageDto responseMessage = new ResponseMessageDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
    }

    @ExceptionHandler(MoreThanOneRangeException.class)
    public ResponseEntity<ResponseMessageDto> handleMoreThanOneDiapason(MoreThanOneRangeException ex) {
        ResponseMessageDto responseMessage = new ResponseMessageDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ResponseMessageDto> handleDatabaseOperation(DatabaseOperationException ex) {
        ResponseMessageDto responseMessage = new ResponseMessageDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
    }
}
