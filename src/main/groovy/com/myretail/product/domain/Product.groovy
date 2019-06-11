package com.myretail.product.domain

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString
class Product {
    Long id
    String name

    @JsonProperty('current_price')
    CurrentPrice currentPrice

    @Override
    String toString() {
        return "id= $id, name=$name, price=${currentPrice.value}, currency=${currentPrice.currencyCode}"
    }
}
