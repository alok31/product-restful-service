package com.myretail.product.controller

import com.myretail.product.domain.Product
import com.myretail.product.entity.ProductPrice
import com.myretail.product.exception.InternalServerException
import com.myretail.product.exception.ProductNotFoundException
import com.myretail.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = 'api/v1/products')
class ProductController {

    @Autowired
    ProductService productService

    @GetMapping(value = '{id}')
    Product fetchAggregatedProductData(@PathVariable Long id) throws InternalServerException, ProductNotFoundException {
        return productService.fetchAggregatedProductData(id)
    }

    @PutMapping(value = '{id}', produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity updateProductPrice(@PathVariable Long id, @RequestBody Product product) throws ProductNotFoundException {
        return productService.updateProductPrice(id, product)
    }

    @GetMapping(value = '{id}/price')
    ProductPrice fetchProductPrice(@PathVariable Long id) {
        return productService.fetchProductPrice(id)
    }

    @GetMapping(value = '{id}/name')
    String fetchProductName(@PathVariable Long id) throws InternalServerException, ProductNotFoundException {
        return productService.fetchProductName(id)
    }

}
