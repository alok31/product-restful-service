package com.myretail.product.controller

import com.myretail.product.domain.Product
import com.myretail.product.entity.ProductPrice
import com.myretail.product.service.ProductService
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class ProductControllerSpec extends Specification {
    ProductController sut = new ProductController(
            productService: Mock(ProductService)
    )

    def 'FetchAggregatedProductData'() {
        setup:
        Long id = 111L
        Product product = [id: id, name: 'testing product', currentPrice: [value: 10.99, currencyCode: 'USD']]

        when:
        def result = sut.fetchAggregatedProductData(id)

        then:
        1 * sut.productService.fetchAggregatedProductData(id) >> product
        0 * _
        with(result) {
            id == 111L
            name == 'testing product'
            currentPrice.value == 10.99
            currentPrice.currencyCode == 'USD'
        }
    }

    def 'UpdateProductPrice'() {
        setup:
        Long id = 111L
        Product product = [id: id, name: 'testing product', currentPrice: [value: 10.99, currencyCode: 'USD']]
        ResponseEntity mockedResponseEntity = Mock(ResponseEntity)

        when:
        sut.updateProductPrice(id, product)

        then:
        1 * sut.productService.updateProductPrice(id, product) >> mockedResponseEntity
        0 * _
    }

    def 'FetchProductPrice'() {
        setup:
        Long id = 111L
        ProductPrice productPrice = [id: id, currentPrice: 10.99, currencyCode: 'USD']

        when:
        def result = sut.fetchProductPrice(id)

        then:
        1 * sut.productService.fetchProductPrice(id) >> productPrice
        0 * _
        with(result) {
            id == 111L
            currentPrice == 10.99
            currencyCode == 'USD'
        }
    }

    def 'FetchProductName'() {
        setup:
        Long id = 111L

        when:
        def result = sut.fetchProductName(id)

        then:
        1 * sut.productService.fetchProductName(id) >> 'test product name'
        0 * _
        result == 'test product name'
    }
}
