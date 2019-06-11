# product-restful-service
Application provides aggregated product Information gathered from different sources for given product ID. 

This application consists below layers:
1. Controller layer for endpoints 
     -ProductController
2. Service layer for all business needs
     -ProductService
3. Persistence layer for DB crud operations 
     -ProductRepository, 
     -Entity: ProductPrice
     -DTO : Product and CurrentPrice
4. Configuration
     - MongoDatabaseConfig
     - RestTemplateConfig

Technology Stack Used:
1.Springboot           2.Groovy
3.Embedded Mongo DB    4.Spring REST/ RestTemplate
5.Gradle               6.Spock
7.JaCoCo               8.Spring data
9 SLF4j logging 	  10.Embedded Tomcat

This app has been hosted on Heroku so can be directly accessed using URL - https://product-restful-service.herokuapp.com/

Build and run the application locally:
1. Please clone or download the source code from Github. 
2. Unzip the downloaded source code and go to extracted folder 
3. Please build the app using './gradlew clean build'. JaCoCo will generate code coverage report. It can found here - /build/reports/tests/test/index.html
4. Start the application using - './gradlew bootrun'
5. This springboot application will be up and running. 
6. Please verify using application landing Endpoint - http://localhost:8080/.

Testing tool Needed:
Get APIs can be directly called from browser. POST/PUT API can be tested using Postman or terminal (curl command can be used)

APIs Used in this Application
1. Get Aggregated Product Info (Product name and price) by giving id
	GET http://localhost:8080/api/v1/products/{id}
	Request - curl http://localhost:8080/api/v1/products/13860428
	Response - {"id":13860428,"name":"The Big Lebowski (Blu-ray)","current_price":{"value":0.99,"currency_code":"USD"}}
	Note: if product name is available and price details not found in DB then default Product price value(0.99/USD) will be inserted in DB and returns the aggregated product information. 
2. Update the Product Price by id 
	PUT http://localhost:8080/api/v1/products/{id}
	Request - curl -X PUT -H "Content-Type: application/json" -d '{"id":13860428,"name":"The Big Lebowski (Blu-ray)","current_price":{"value":1000.99,"currency_code":"USD"}}'' http://localhost:8080/api/v1/products/13860428
	Response - Status code 200 OK
	Call the get method to verify the update
3. Additional Helpful APIs 
   	1. 	Get only product name from external api
   		GET http://localhost:8080/api/v1/products/{id}/name
   		Request - curl http://localhost:8080/api/v1/products/13860428/name
		Response - The Big Lebowski (Blu-ray)
	2. 	Get only price information saved in Mongodb 
		GET http://localhost:8080/api/v1/products/{id}/price
   		Request - curl http://localhost:8080/api/v1/products/13860428/price
		Response - {"id":13860428,"currentPrice":1000.99,"currencyCode":"USD"}