package com.inventApper.flashkart.services;

import com.inventApper.flashkart.dtos.CategoryDto;
import com.inventApper.flashkart.dtos.PageableResponse;

public interface CategoryService {

    // create category
    CategoryDto createCategory(CategoryDto categoryDto);

    // update Category
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    // delete Category
    void deleteCategory(String categoryId);

    // get all Category
    PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get single category detail
    CategoryDto getSingleCategory(String categoryId);

    // search

}
