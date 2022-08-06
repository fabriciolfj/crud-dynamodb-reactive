package com.github.fabriciolfj.productservice.service;

import com.github.fabriciolfj.productservice.model.Category;
import com.github.fabriciolfj.productservice.model.Product;
import com.github.fabriciolfj.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Mono<Void> save(final Product product) {
        return Mono.fromFuture(productRepository.save(product))
                .doOnSuccess(e -> log.info("Product created"))
                .doOnError(e -> log.error("Fail created product: {}", e.getMessage()));
    }

    public Mono<Product> findById(final String id) {
        return Mono.fromFuture(productRepository.get(id))
                .doOnError(e-> log.error("Fail find product : {}, details: {}", id, e.getMessage()));
    }

    public Mono<Category> findByCategory(final String id) {
        return Mono.from(productRepository.getProductCategory(id)
                        .items()
                        .map(Product::getCategory));
    }

    public Mono<Product> update(final Product product) {
        return Mono.fromFuture(productRepository.update(product))
                .doOnSuccess(e -> log.info("Product update: {}", e))
                .doOnError(e -> log.error("Fail update product: {}", e.getMessage()));
    }

    public Mono<Void> delete(final String id) {
        return Mono.fromFuture(productRepository.delete(id))
                .then();
    }

    public Flux<Product> list() {
        return Flux.from(productRepository.getAll().items());
    }
}
