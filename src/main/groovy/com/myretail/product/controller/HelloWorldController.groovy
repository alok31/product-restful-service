package com.myretail.product.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {

    @GetMapping('/')
    String helloWorld() {
        return 'Hello World from Product Restful Service'
    }
}
