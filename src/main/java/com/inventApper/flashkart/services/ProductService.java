package com.inventApper.flashkart.services;

import com.inventApper.flashkart.dtos.PageableResponse;
import com.inventApper.flashkart.dtos.ProductDto;

public interface ProductService {

    // create product
    ProductDto createProduct(ProductDto productDto);

    // update product
    ProductDto updateProduct(ProductDto productDto, String productId);

    // delete product
    void deleteProduct(String productId);

    // get single
    ProductDto get(String productId);

    // get all product
    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get all live
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    // search product
    PageableResponse<ProductDto> searchByTitle(String title, int pageNumber, int pageSize, String sortBy,
            String sortDir);

}
