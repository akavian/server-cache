package com.ali.server.caching.advice

import com.ali.server.caching.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ResourceAdvice {

    @ExceptionHandler
    fun handleResourceNotFoundException(exception: ResourceNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.message)
    }