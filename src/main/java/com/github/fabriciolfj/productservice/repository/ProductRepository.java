package com.github.fabriciolfj.productservice.repository;

import com.github.fabriciolfj.productservice.model.Product;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.concurrent.CompletableFuture;

@Repository
public class ProductRepository {

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<Product> productDynamoDbAsyncTable;

    public ProductRepository(final DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        this.enhancedAsyncClient = enhancedAsyncClient;
        this.productDynamoDbAsyncTable = enhancedAsyncClient.table(Product.class.getSimpleName(), TableSchema.fromBean(Product.class));
    }


    public CompletableFuture<Void> save(final Product product) {
        return productDynamoDbAsyncTable.putItem(product);
    }

    public CompletableFuture<Product> get(final String id) {
        return productDynamoDbAsyncTable.getItem(getKeyBuild(id));
    }

    public CompletableFuture<Product> update(final Product product) {
        return productDynamoDbAsyncTable.updateItem(product);
    }

    public CompletableFuture<Product> delete(final String id) {
        return productDynamoDbAsyncTable.deleteItem(getKeyBuild(id));
    }

    public PagePublisher<Product> getProductCategory(final String id) {
        return productDynamoDbAsyncTable
                .query(r -> r.queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                        .addAttributeToProject("category"));
    }

    public PagePublisher<Product> getAll() {
        return productDynamoDbAsyncTable.scan();
    }

    public Key getKeyBuild(final String id) {
        return Key.builder().partitionValue(id).build();
    }
}
