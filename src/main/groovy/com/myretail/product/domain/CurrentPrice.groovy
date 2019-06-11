package com.myretail.product.domain

import com.fasterxml.jackson.annotation.JsonProperty

class CurrentPrice {
    double value

    @JsonProperty('currency_code')
    String currencyCode
}
