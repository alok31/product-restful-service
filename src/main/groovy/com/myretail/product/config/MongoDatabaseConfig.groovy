package com.myretail.product.config

import com.mongodb.MongoClient
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate


@Configuration
class MongoDatabaseConfig {

    @Value('${mongo.database.url}')
    private String MONGO_DATABASE_URL

    @Value('${mongo.database.name}')
    private String MONGO_DATABASE_NAME

    @Bean
    MongoTemplate mongoTemplate() throws IOException {
        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean()
        mongo.setBindIp(MONGO_DATABASE_URL)
        MongoClient mongoClient = mongo.getObject()
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DATABASE_NAME)
        return mongoTemplate
    }
}
