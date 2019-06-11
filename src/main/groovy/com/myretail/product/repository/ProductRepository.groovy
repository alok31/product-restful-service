package com.myretail.product.repository


import com.myretail.product.entity.ProductPrice
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository extends MongoRepository<ProductPrice, Long> {
    ProductPrice findProductPriceById(Long id)
}
