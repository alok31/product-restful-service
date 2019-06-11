package com.myretail.product.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerException extends RuntimeException {

    InternalServerException(String message) {
        super(message)
    }
}
