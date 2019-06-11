package com.myretail.product.service

import com.myretail.product.domain.Product
import com.myretail.product.entity.ProductPrice
import com.myretail.product.exception.BadRequestException
import com.myretail.product.exception.ProductNotFoundException
import com.myretail.product.repository.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Unroll

class ProductServiceSpec extends Specification {

    ProductService sut = new ProductService(
            productRepository: Mock(ProductRepository),
            restTemplate: Mock(RestTemplate),
            excludesQueryParam: 'abc,def,ghi,jkl,mno',
            hostUrl: 'abc.com',
            baseUrl: 'v1/api/'
    )
    ResponseEntity mockResponseEntity = Mock(ResponseEntity)
    String externalApiResponse = """{
  "product": {
    "item": {
      "product_description": {
        "title": "interesting test product"
      }
    }
  }
}"""

    def "FetchAggregatedProductData"() {
        setup:
        Long id = 111L
        String url = 'https://abc.com/v1/api/111?excludes=abc,def,ghi,jkl,mno'
        ProductPrice productPrice = [id: id, currentPrice: 10.99, currencyCode: 'USD']
        Product product = [id: id, name: 'interesting test product', currentPrice: [value: 10.99, currencyCode: 'USD']]

        when:
        Product resultProduct = sut.fetchAggregatedProductData(id)

        then:
        1 * sut.restTemplate.getForEntity(url, String) >> mockResponseEntity
        1 * mockResponseEntity.body >> externalApiResponse
        1 * sut.productRepository.findProductPriceById(id) >> productPrice
        0 * _
        resultProduct.id == product.id
        resultProduct.name == product.name
        resultProduct.currentPrice.value == product.currentPrice.value
        resultProduct.currentPrice.currencyCode == product.currentPrice.currencyCode
    }

    def "FetchAggregatedProductData - get default price info"() {
        setup:
        Long id = 111L
        String name = 'interesting test product'
        String url = 'https://abc.com/v1/api/111?excludes=abc,def,ghi,jkl,mno'

        when:
        Product resultProduct = sut.fetchAggregatedProductData(id)

        then:
        1 * sut.restTemplate.getForEntity(url, String) >> mockResponseEntity
        1 * mockResponseEntity.body >> externalApiResponse
        1 * sut.productRepository.findProductPriceById(id) >> null
        1 * sut.productRepository.save(_)
        0 * _
        resultProduct.id == id
        resultProduct.name == name
        resultProduct.currentPrice.value == 0.99
        resultProduct.currentPrice.currencyCode == 'USD'
    }

    def "FetchAggregatedProductData - failure scenario"() {
        setup:
        Long id = 111L
        String url = 'https://abc.com/v1/api/111?excludes=abc,def,ghi,jkl,mno'

        when:
        sut.fetchAggregatedProductData(id)

        then:
        1 * sut.restTemplate.getForEntity(url, String) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        thrown(ProductNotFoundException)
    }

    def "FetchProductName"() {
        setup:
        Long id = 111L
        String url = 'https://abc.com/v1/api/111?excludes=abc,def,ghi,jkl,mno'

        when:
        String productName = sut.fetchProductName(id)

        then:
        1 * sut.restTemplate.getForEntity(url, String) >> mockResponseEntity
        1 * mockResponseEntity.body >> externalApiResponse
        0 * _
        productName == 'interesting test product'
    }

    @Unroll
    def "FetchProductName - failure scenario #statuscode"() {
        setup:
        Long id = 111L
        String url = 'https://abc.com/v1/api/111?excludes=abc,def,ghi,jkl,mno'

        when:
        sut.fetchProductName(id)

        then:
        1 * sut.restTemplate.getForEntity(url, String) >> { throw new HttpClientErrorException(statuscode) }
        0 * _
        thrown(exceptionThrown)

        where:
        statuscode             | exceptionThrown
        HttpStatus.NOT_FOUND   | ProductNotFoundException
        HttpStatus.BAD_REQUEST | BadRequestException

    }

    def "FetchProductPrice"() {
        setup:
        Long id = 111L
        ProductPrice productPrice = [id: id, currentPrice: 10.99, currencyCode: 'USD']

        when:
        ProductPrice result = sut.fetchProductPrice(id)

        then:
        1 * sut.productRepository.findProductPriceById(id) >> productPrice
        0 * _
        with(result) {
            id == productPrice.id
            currentPrice == productPrice.currentPrice
            currencyCode == productPrice.currencyCode
        }
    }

    def "FetchProductPrice exception: product price not found"() {
        setup:
        Long id = 111L

        when:
        sut.fetchProductPrice(id)

        then:
        1 * sut.productRepository.findProductPriceById(id) >> null
        0 * _
        thrown(ProductNotFoundException)

    }

    def "UpdateProductPrice"() {
        setup:
        Long id = 111L
        ProductPrice productPrice = [id: id, currentPrice: 10.99, currencyCode: 'USD']
        Product product = [id: id, name: 'interesting test product', currentPrice: [value: 10.99, currencyCode: 'USD']]

        when:
        ResponseEntity responseEntity = sut.updateProductPrice(id, product)

        then:
        1 * sut.productRepository.findProductPriceById(id) >> productPrice
        1 * sut.productRepository.save(_) >> mockResponseEntity
        0 * _
        responseEntity.statusCode == HttpStatus.OK
    }

    def "UpdateProductPrice exception:404 Not Found"() {
        setup:
        Long id = 111L
        Product product = [id: id, name: 'interesting test product', currentPrice: [value: 10.99, currencyCode: 'USD']]

        when:
        ResponseEntity responseEntity = sut.updateProductPrice(id, product)

        then:
        1 * sut.productRepository.findProductPriceById(id) >> null
        0 * _
        thrown(ProductNotFoundException)
    }

    def "BuildURI"() {
        setup:
        Long id = 111L
        String url = 'https://abc.com/v1/api/111?excludes=abc,def,ghi,jkl,mno'

        when:
        String resultURL = sut.buildURI(id)

        then:
        0 * _
        resultURL == url
    }
}
