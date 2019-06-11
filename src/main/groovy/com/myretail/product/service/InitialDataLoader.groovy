package com.myretail.product.service

import com.myretail.product.domain.Product
import com.myretail.product.entity.ProductPrice
import com.myretail.product.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

@Service
class InitialDataLoader {

    @Autowired
    ProductRepository productRepository

    @PostConstruct
    void loadInitialData() {
        ProductPrice productPrice1 = [id: 13860428, currentPrice: 100.99, currencyCode: 'USD']
        ProductPrice productPrice2 = [id: 15643793, currentPrice: 200.99, currencyCode: 'USD']
        List<Product> productsPrice = [productPrice1, productPrice2]
        productRepository.saveAll(productsPrice)
        println productsPrice.toString()
    }
}
