package com.github.fabriciolfj.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;


@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private String id;
    private String describe;
    private Category category;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("product_id")
    public String getId() {
        return id;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbAttribute("category")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
