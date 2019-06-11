package com.myretail.product.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ProductNotFoundException extends RuntimeException {

    ProductNotFoundException(String message) {
        super(message)
    }
}
