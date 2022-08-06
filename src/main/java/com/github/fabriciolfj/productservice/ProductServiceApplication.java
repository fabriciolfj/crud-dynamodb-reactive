package com.github.fabriciolfj.productservice;

import com.github.fabriciolfj.productservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class ProductServiceApplication implements CommandLineRunner {

	@Autowired
	private DynamoDbAsyncClient asyncClient;
	@Autowired
	private DynamoDbEnhancedAsyncClient enhancedAsyncClient;

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CompletableFuture<ListTablesResponse> listTablesResponseCompletableFuture = asyncClient.listTables();
		CompletableFuture<List<String>> listCompletableFuture = listTablesResponseCompletableFuture.thenApply(ListTablesResponse::tableNames);
		listCompletableFuture.thenAccept(tables -> {
			if (Objects.nonNull(tables) && !tables.contains(Product.class.getSimpleName())) {
				DynamoDbAsyncTable<Product> product = enhancedAsyncClient.table(Product.class.getSimpleName(), TableSchema.fromBean(Product.class));
				product.createTable();
			}
		});
	}
}
