package com.myretail.product.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.product.domain.CurrentPrice
import com.myretail.product.domain.Product
import com.myretail.product.entity.ProductPrice
import com.myretail.product.exception.BadRequestException
import com.myretail.product.exception.InternalServerException
import com.myretail.product.exception.ProductNotFoundException
import com.myretail.product.repository.ProductRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class ProductService {

    static final Logger log = LoggerFactory.getLogger(ProductService)

    @Autowired
    RestTemplate restTemplate

    @Autowired
    ProductRepository productRepository

    @Value('${excludes.query.param}')
    String excludesQueryParam

    @Value('${base.url}')
    String baseUrl

    @Value('${host.url}')
    String hostUrl

    static final NODE_NAME = '/product/item/product_description/title'

    Product fetchAggregatedProductData(Long id) throws ProductNotFoundException, InternalServerException {
        log.info('Inside fetchAggregatedProductData method to aggregate product name and  price detail')
        Product product
        try {
            //fetching product name from external API
            String productName = fetchProductName(id)
            if (!productName) {
                throw new ProductNotFoundException("Product not found from external API, id=$id")
            }
            product = [id: id, name: productName]

            // fetching product details from Mongo DB
            ProductPrice productPrice = productRepository.findProductPriceById(id)

            if (productPrice) {
                product.currentPrice = new CurrentPrice(value: productPrice.currentPrice, currencyCode: productPrice.currencyCode)
            } else {
                //inserting the default Price to DB if its not there for valid product Id
                ProductPrice defaultPrice
                log.info("Product price does not exist. Inserting the default product CurrentPrice (0.99/USD) only if Product name already exist")
                defaultPrice = [id: id, currentPrice: 0.99, currencyCode: "USD"]
                productRepository.save(defaultPrice)
                product.currentPrice = new CurrentPrice(value: defaultPrice.currentPrice, currencyCode: defaultPrice.currencyCode)

            }
            log.info("Product details successfully fetched and aggregated")
        } catch (ProductNotFoundException | InternalServerException | BadRequestException ex) {
            throw ex
        } catch (any) {
            log.debug("Unknown exception occurred while fetching aggregated product data")
            throw new InternalServerException(any.message)
        }
        return product
    }

    String fetchProductName(Long id) {
        String productName
        String URL = buildURI(id)
        log.info("External API is being called to fetch product name, url= $URL, productId= $id")
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(URL, String)
            ObjectMapper mapper = new ObjectMapper()
            JsonNode jsonNode = mapper.readTree(response.getBody())
            JsonNode nodeName = jsonNode.at(NODE_NAME)
            productName = nodeName.textValue()
            log.info("Successfully fetched Product Name from external API, product name : $productName , product id: $id")
        } catch (HttpClientErrorException exception) {
            log.error("Exception occurred while getting product name, Exception - ${exception.message}")
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ProductNotFoundException("Could not found product from external API, product id:$id")
            } else {
                throw new BadRequestException(exception.message)
            }
        } catch (any) {
            log.error("Unknown exception occurred , exception= ${any.message}")
            throw new InternalServerException(any.message)
        }
        return productName
    }

    ProductPrice fetchProductPrice(Long id) {
        log.info("Inside fetchProductPrice method")
        ProductPrice productPrice = productRepository.findProductPriceById(id)
        if (!productPrice) {
            log.error("Product not found in database, id= $id")
            throw new ProductNotFoundException("Product information is not found in Mongodb")
        }
        log.info("Product price details successfully fetched from DB")
        return productPrice
    }

    ResponseEntity updateProductPrice(Long id, Product product) {
        log.info("Product CurrentPrice is being updated")
        ProductPrice productPrice = productRepository.findProductPriceById(id)
        if (productPrice) {
            productPrice.currencyCode = product.currentPrice.currencyCode
            productPrice.currentPrice = product.currentPrice.value
            productRepository.save(productPrice)
            log.info("Product CurrentPrice has been updated")
            return new ResponseEntity(HttpStatus.OK)
        } else {
            String message = "Product Not found in database, id=$id"
            log.error(message)
            throw new ProductNotFoundException(message)
        }
    }

    String buildURI(Long id) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(hostUrl)
                .path(baseUrl + id)
                .query("excludes={excludesQueryParam}")
                .buildAndExpand(excludesQueryParam)
                .toUriString()
    }
}
