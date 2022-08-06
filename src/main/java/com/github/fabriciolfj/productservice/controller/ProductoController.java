package com.github.fabriciolfj.productservice.controller;

import com.github.fabriciolfj.productservice.model.Category;
import com.github.fabriciolfj.productservice.model.Product;
import com.github.fabriciolfj.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductoController {

    private final ProductService productService;

    @GetMapping
    public Flux<Product> findAll() {
        return productService.list();
    }

    @GetMapping("/{id}/category")
    public Mono<ResponseEntity<Category>> getCategory(@PathVariable("id") final String id) {
        return productService.findByCategory(id)
                .map(s -> ResponseEntity.accepted().body(s))
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> get(@PathVariable("id") final String id) {
        return productService.findById(id)
                .map(p -> ResponseEntity.accepted().body(p))
                .switchIfEmpty(Mono.defer(() -> Mono.just(ResponseEntity.notFound().build())))
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> save(@RequestBody final Product product) {
        return productService.save(product)
                .map(v -> ResponseEntity.noContent().build())
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @PutMapping
    public Mono<ResponseEntity<Product>> updateProduct(@RequestBody final Product product) {
        return productService.update(product)
                .map(v -> ResponseEntity.accepted().body(v))
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") final String id) {
        return productService.delete(id);
    }
}
