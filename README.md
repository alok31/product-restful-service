# product-restful-service
Application provides aggregated product Information gathered from different sources for given product ID. 

This application consist of 3 layers:
1. Controller layer for endpoints
2. Service layer for all business need
3. Persistence layer for DB crud operation

Technology Stack Used:
1.Springboot           2.Groovy
3.Imbedded Mongo DB    4.Spring REST/ RestTemplate
5.Gradle               6.Spock
7.JaCoCo               8.Spring data
9 SLF4j logging 	  10.Imbedded Tomcat

This app has been hosted on Heroku so can be directly accessed using https://product-restful-service.herokuapp.com/

Build and run the application locally:
1. Please clone or download the source code from Github. 
2. Unzip the downloaded source code and go to extracted folder 
3. Start the application using - './gradlew bootrun' 
4. This springboot application will be up and running. 
5. Please verify using application default Endpoint - http://localhost:8080/


APIs Used in this Application
1. Get Aggregated Product Info (Product name and price) by giving id
	GET http://localhost:8080/api/v1/products/{id}
	Request - curl http://localhost:8080/api/v1/products/13860428
	Response - {"id":13860428,"name":"The Big Lebowski (Blu-ray)","current_price":{"value":0.99,"currency_code":"USD"}}
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

Code Coverage: 
JACoCo tool is being used to get coverage report. Please build the app using './gradle clean build'. Report can found here - /build/reports/tests/test/index.html

Note: if product name is available and price details not found in DB then default Product price value(0.99/USD) will be inseerted in DB and returns to API call only. 