package com.myretail.product.entity

import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@ToString
class ProductPrice {
    @Id
    Long id
    double currentPrice
    String currencyCode
}
